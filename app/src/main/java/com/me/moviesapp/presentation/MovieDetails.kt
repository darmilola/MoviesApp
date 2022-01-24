package com.me.moviesapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.me.moviesapp.R
import com.me.moviesapp.data.MoviesModel

class MovieDetails : AppCompatActivity() {

    lateinit var title : TextView
    lateinit var overView : TextView
    lateinit var rating : TextView
    lateinit var releaseDate : TextView
    lateinit var poster : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        initView()
    }

    private fun initView(){
        title = findViewById(R.id.details_title)
        overView = findViewById(R.id.details_overview)
        rating = findViewById(R.id.details_rating)
        releaseDate = findViewById(R.id.details_release_date)
        poster = findViewById(R.id.details_image)

        var model: MoviesModel? = getIntent().getParcelableExtra<MoviesModel>("details")
        title.text = model!!.title
        overView.text = model!!.overview
        rating.text = model!!.voteAverage.toString()
        releaseDate.text = model!!.releaseDate

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/"+model.posterPath)
            .into(poster)
    }
}