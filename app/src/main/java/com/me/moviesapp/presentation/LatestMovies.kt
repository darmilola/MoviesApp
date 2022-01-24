package com.me.moviesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.me.moviesapp.R
import com.me.moviesapp.data.*
import com.me.moviesapp.presentation.ViewModels.LatestViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observer

@AndroidEntryPoint
class LatestMovies : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var latestViewModel: LatestViewModel
    var movieslist: MutableList<MoviesModel> = ArrayList()
    lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_movies)
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
                    progressBar.visibility = View.GONE
                    SetupSnackbar()
                }
            }
        })
    }

    private fun SetupSnackbar(){

        var snackbar : Snackbar = Snackbar.make(coordinatorLayout,"Error Occured Please Try Again", Snackbar.LENGTH_INDEFINITE)
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
        movieslist.add(moviesModel)
        moviesAdapter.addData(movieslist)
        moviesAdapter.notifyDataSetChanged()
    }
}