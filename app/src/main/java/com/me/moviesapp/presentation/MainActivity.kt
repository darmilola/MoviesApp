package com.me.moviesapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.me.moviesapp.R

class MainActivity : AppCompatActivity() {
    lateinit var showLatestMovies: MaterialButton
    lateinit var showPopularMovies: MaterialButton
    lateinit var showUpcomingMovies: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView(){
        showLatestMovies = findViewById(R.id.show_latest_movies)
        showPopularMovies = findViewById(R.id.show_popular_movies)
        showUpcomingMovies = findViewById(R.id.show_upcoming_movies)

        showLatestMovies.setOnClickListener {
            startActivity(Intent(this@MainActivity,LatestMovies::class.java))
        }

        showPopularMovies.setOnClickListener {
            startActivity(Intent(this@MainActivity,PopularMovies::class.java))
        }

        showUpcomingMovies.setOnClickListener {
            startActivity(Intent(this@MainActivity,UpcomingMovies::class.java))
        }
    }
}