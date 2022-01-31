package com.me.moviesapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.me.moviesapp.R
import com.me.moviesapp.data.ApiHelper
import com.me.moviesapp.data.Database.Database
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.MoviesModel
import com.me.moviesapp.data.Status
import com.me.moviesapp.data.ViewModelFactory
import com.me.moviesapp.databinding.FragmentLatestBinding
import com.me.moviesapp.presentation.viewModels.LatestViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class Latest : Fragment() {
    private var mLatestViewModel: LatestViewModel? = null
    private var binding: FragmentLatestBinding? = null
    private lateinit var mView: View
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var db: Database
    var movieslist: MutableList<MoviesModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest, container, false)
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
            Database::class.java, "latestMovies"
        ).build()

        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        moviesAdapter = MoviesAdapter(arrayListOf())
        binding!!.recyclerView.adapter = moviesAdapter


        binding!!.swiperefresh.setOnRefreshListener {
            mLatestViewModel!!.fetchMovies(requireContext())
        }

    }

    private fun setupObserver() {
        mLatestViewModel!!.getLatestMovies().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding!!.swiperefresh.isRefreshing = false
                    Log.e("Test", it.data.toString())
                    it.data?.let { moviesModel: MoviesModel -> renderList(moviesModel) }
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
        mLatestViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper())
        ).get(LatestViewModel::class.java)

        mLatestViewModel!!.fetchMovies(requireContext())
    }

    private fun SetupSnackbar(message: CharSequence?){
        var snackbar : Snackbar = Snackbar.make(
            binding!!.rootLayout,
            message!!,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Retry", View.OnClickListener { mLatestViewModel!!.fetchMovies(requireContext()) })
        val view: View = snackbar.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snackbar.show()
    }


    private fun renderList(moviesModel: MoviesModel) {

        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(requireContext())
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
            val latestMoviesEntity = LatestMoviesEntity(
                moviesModel.id,
                moviesModel.title,
                moviesModel.overview,
                moviesModel.voteAverage,
                moviesModel.posterPath,
                moviesModel.releaseDate
            )
            movieList.add(latestMoviesEntity)
            db.latestMovieDao().insertAll(movieList)
        }

    }

}