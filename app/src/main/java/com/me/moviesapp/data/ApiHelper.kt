package com.me.moviesapp.data

import com.me.moviesapp.domain.ApiService
import javax.inject.Inject


class ApiHelper {
    @Inject private val apiService: ApiService? = null

    fun getUpcomingMovie() = apiService!!.getUpComingMovies()

    fun getLatestMovies() = apiService!!.getLatestMovies()

    fun getPopularMovies() = apiService!!.getPopularMovies()
}