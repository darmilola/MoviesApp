package com.me.moviesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.me.moviesapp.R
import com.me.moviesapp.data.*
import com.me.moviesapp.data.Database.Database
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.presentation.ViewModels.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularMovies : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var popularViewModel: PopularViewModel
    var totalPageCount: Int = 0
    var currentPage: Int  = 0
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movies)
        initView()
        setupViewModel()
        setupObserver()
    }

    private fun initView() {

        db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "popularMovies"
        ).build()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        coordinatorLayout = findViewById(R.id.rootLayout)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        moviesAdapter = MoviesAdapter(arrayListOf())
        recyclerView.adapter = moviesAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(currentPage < totalPageCount){
                        popularViewModel.fetchMoreMovies(currentPage + 1)
                    }
                }
            }
        })
    }


    private fun setupObserver() {
        popularViewModel.getPopularMovies().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let {
                            moviesResult: MoviesResult -> renderList(moviesResult)
                    }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    SetupSnackbar(it.message)
                }
            }
        })
    }

    private fun SetupSnackbar(message: CharSequence?){

        var snackbar : Snackbar = Snackbar.make(coordinatorLayout,message!!, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry",View.OnClickListener { popularViewModel.fetchMovies(this) })
        snackbar.show()

    }

    private fun setupViewModel() {
        popularViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper())
        ).get(PopularViewModel::class.java)
        popularViewModel.fetchMovies(this)
    }

    private fun renderList(moviesResult: MoviesResult) {
        totalPageCount = moviesResult.totalPage
        currentPage = moviesResult.currentPage
        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(this)
        if(!isAvailable && moviesAdapter.getData().size != 0){
        }
        else{
            moviesAdapter.addData(moviesResult.data)
            moviesAdapter.notifyDataSetChanged()
        }

        var movieList: ArrayList<PopularMoviesEntity>  = arrayListOf()
        var movieModels = moviesResult.data

        GlobalScope.launch {
            for(movieModel in movieModels){
                val popularMoviesEntity = PopularMoviesEntity(movieModel.id,movieModel.title,movieModel.overview,movieModel.voteAverage,movieModel.posterPath,movieModel.releaseDate)
                movieList.add(popularMoviesEntity)
            }
            db.popularMovieDao().insertAll(movieList)
        }
    }
}