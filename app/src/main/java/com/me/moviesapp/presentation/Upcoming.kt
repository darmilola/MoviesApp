package com.me.moviesapp.presentation

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.me.moviesapp.R
import com.me.moviesapp.data.*
import com.me.moviesapp.data.Database.Database
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import com.me.moviesapp.databinding.FragmentUpcomingBinding
import com.me.moviesapp.presentation.viewModels.UpcomingViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class Upcoming : Fragment() {
    private var mUpcomingViewModel: UpcomingViewModel? = null
    private var binding: FragmentUpcomingBinding? = null
    private lateinit var mView: View
    private lateinit var progressBar: ProgressBar
    private lateinit var moviesAdapter: MoviesAdapter
    private var totalPageCount: Int = 0
    private var currentPage: Int  = 0
    private lateinit var db: Database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming, container, false)
        mView = binding!!.getRoot()
        setupViewModel()
        initView()
        setupObserver()
        return mView
    }

    private fun initView(){

        binding!!.setLifecycleOwner(this)

        db = Room.databaseBuilder(
            requireContext(),
            Database::class.java, "upcomingMovies"
        ).build()

        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        moviesAdapter = MoviesAdapter(arrayListOf())
        binding!!.recyclerView.adapter = moviesAdapter

        binding!!.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(currentPage < totalPageCount){
                        mUpcomingViewModel!!.fetchMoreMovies(currentPage + 1)
                    }
                }
            }
        })

        binding!!.swiperefresh.setOnRefreshListener {
            mUpcomingViewModel!!.fetchMovies(requireContext())
        }

    }

    private fun setupObserver() {
        mUpcomingViewModel!!.getUpcomingMovies().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding!!.swiperefresh.isRefreshing = false
                    it.data?.let {
                            moviesResult: MoviesResult -> renderList(moviesResult.data)
                        totalPageCount = moviesResult.totalPage
                        currentPage = moviesResult.currentPage
                    }
                    binding!!.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding!!.swiperefresh.isRefreshing = true
                    binding!!.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    binding!!.recyclerView.visibility = View.VISIBLE
                    binding!!.swiperefresh.isRefreshing = false
                    SetupSnackbar(it.message)
                }
            }
        })
    }

    private fun setupViewModel() {
        mUpcomingViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper())
        ).get(UpcomingViewModel::class.java)

        mUpcomingViewModel!!.fetchMovies(requireContext())
    }

    private fun SetupSnackbar(message: CharSequence?){
        var snackbar : Snackbar = Snackbar.make(
            binding!!.rootLayout,
            message!!,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Retry", View.OnClickListener { mUpcomingViewModel!!.fetchMovies(requireContext()) })
        val view: View = snackbar.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackbar.show()
    }


    private fun renderList(moviesModels: ArrayList<MoviesModel>) {
        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(requireContext())
        if(!isAvailable && moviesAdapter.getData().size != 0){
        }
        else{
            moviesAdapter.addData(moviesModels)
            moviesAdapter.notifyDataSetChanged()
        }

        var movieList: ArrayList<UpcomingMoviesEntity>  = arrayListOf()
        GlobalScope.launch {

            for(movieModel in moviesModels){
                val upcomingMoviesEntity = UpcomingMoviesEntity(
                    movieModel.id,
                    movieModel.title,
                    movieModel.overview,
                    movieModel.voteAverage,
                    movieModel.posterPath,
                    movieModel.releaseDate
                )
                movieList.add(upcomingMoviesEntity)
            }
            db.upcomingMovieDao().insertAll(movieList)
        }
    }

}