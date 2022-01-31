package com.me.moviesapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.me.moviesapp.R
import com.me.moviesapp.data.MoviesModel
import com.me.moviesapp.databinding.ActivityMovieDetailsBinding
import com.me.moviesapp.databinding.FragmentUpcomingBinding

class MovieDetails : AppCompatActivity() {

    private var binding: ActivityMovieDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        initView()
    }

    private fun initView(){
        var model: MoviesModel? = intent.getParcelableExtra<MoviesModel>("details")
        binding!!.detailsTitle.text = model!!.title
        binding!!.detailsOverview.text = model!!.overview
        binding!!.detailsRating.text = model!!.voteAverage.toString()
        binding!!.detailsReleaseDate.text = model!!.releaseDate

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/"+model.posterPath)
            .into(binding!!.detailsImage)
    }
}