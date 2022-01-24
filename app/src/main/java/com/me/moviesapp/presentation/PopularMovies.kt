package com.me.moviesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.me.moviesapp.R
import com.me.moviesapp.data.*
import com.me.moviesapp.presentation.ViewModels.LatestViewModel
import com.me.moviesapp.presentation.ViewModels.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMovies : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var popularViewModel: PopularViewModel
    var totalPageCount: Int = 0
    var currentPage: Int  = 0
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_movies)
        initView()
        setupViewModel()
        setupObserver()
    }

    private fun initView() {
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
                    SetupSnackbar()
                }
            }
        })
    }

    private fun SetupSnackbar(){

        var snackbar : Snackbar = Snackbar.make(coordinatorLayout,"Error Occurred Please Try Again", Snackbar.LENGTH_INDEFINITE)
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
        moviesAdapter.addData(moviesResult.data)
        moviesAdapter.notifyDataSetChanged()
    }
}