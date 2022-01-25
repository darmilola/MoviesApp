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
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import com.me.moviesapp.presentation.ViewModels.UpcomingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpcomingMovies : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var upcomingViewModel: UpcomingViewModel
    var totalPageCount: Int = 0
    var currentPage: Int  = 0
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)
        initView()
        setupViewModel()
        setupObserver()
    }

    private fun initView() {
         db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "upcomingMovies"
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
                        upcomingViewModel.fetchMoreMovies(currentPage + 1)
                    }
                }
            }
        })


    }


    private fun setupObserver() {
        upcomingViewModel.getUpcomingMovies().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let {
                            moviesResult: MoviesResult -> renderList(moviesResult.data)
                            totalPageCount = moviesResult.totalPage
                            currentPage = moviesResult.currentPage
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
                    SetupSnackbar(it.message)
                }
            }
        })
    }

    private fun SetupSnackbar(message: CharSequence?){

        var snackbar : Snackbar = Snackbar.make(coordinatorLayout,message!!, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry",View.OnClickListener { upcomingViewModel.fetchMovies(this) })
             snackbar.show()

    }




    private fun setupViewModel() {
        upcomingViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper())
        ).get(UpcomingViewModel::class.java)

        upcomingViewModel.fetchMovies(this)
    }

    private fun renderList(moviesModels: ArrayList<MoviesModel>) {
        moviesAdapter.addData(moviesModels)
        moviesAdapter.notifyDataSetChanged()

       var movieList: ArrayList<UpcomingMoviesEntity>  = arrayListOf()

        GlobalScope.launch {

            for(movieModel in moviesModels){
                val upcomingMoviesEntity = UpcomingMoviesEntity(movieModel.id,movieModel.title,movieModel.overview,movieModel.voteAverage,movieModel.posterPath,movieModel.releaseDate)
                movieList.add(upcomingMoviesEntity)
            }
             db.upcomingMovieDao().insertAll(movieList)

        }
    }
}