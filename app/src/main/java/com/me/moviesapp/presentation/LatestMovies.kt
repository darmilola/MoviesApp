package com.me.moviesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.me.moviesapp.data.Database.LatestDatabase
import com.me.moviesapp.data.Database.UpcomingDatabase
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.presentation.ViewModels.LatestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LatestMovies : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var latestViewModel: LatestViewModel
    var movieslist: MutableList<MoviesModel> = ArrayList()
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_movies)
        initView()
        setupViewModel()
        setupObserver()
    }

    private fun initView() {

        db = Room.databaseBuilder(
            this,
            com.me.moviesapp.data.Database.Database::class.java, "latestMovies"
        ).build()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        coordinatorLayout = findViewById(R.id.rootLayout)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        moviesAdapter = MoviesAdapter(arrayListOf())
        recyclerView.adapter = moviesAdapter
    }


    private fun setupObserver() {
        latestViewModel.getLatestMovies().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e("Test", it.data.toString())
                    progressBar.visibility = View.GONE
                    it.data?.let { moviesModel: MoviesModel -> renderList(moviesModel) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    SetupSnackbar(it.message)
                }
            }
        })
    }

    private fun SetupSnackbar(message: CharSequence?){

        var snackbar : Snackbar = Snackbar.make(coordinatorLayout,message!!, Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry",View.OnClickListener { latestViewModel.fetchMovies(this) })
        snackbar.show()

    }



    private fun setupViewModel() {
        latestViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper())
        ).get(LatestViewModel::class.java)
        latestViewModel.fetchMovies(this)
    }

    private fun renderList(moviesModel: MoviesModel) {

        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(this)
        if(!isAvailable && moviesAdapter.getData().size != 0){
            //Check if Network is not available and already loaded from Room
        }
        else{
            movieslist.add(moviesModel)
            moviesAdapter.addData(movieslist)
            moviesAdapter.notifyDataSetChanged()
        }

        var movieList: ArrayList<LatestMoviesEntity>  = arrayListOf()
        GlobalScope.launch {
                val latestMoviesEntity = LatestMoviesEntity(moviesModel.id,moviesModel.title,moviesModel.overview,moviesModel.voteAverage,moviesModel.posterPath,moviesModel.releaseDate)
                movieList.add(latestMoviesEntity)
                db.latestMovieDao().insertAll(movieList)
            }

    }
}