package com.me.moviesapp.data

import com.me.moviesapp.domain.ApiService
import retrofit2.Retrofit
import javax.inject.Inject


class ApiHelper {
     var retrofit: Retrofit
     var apiService: ApiService

    init{
        retrofit = RetrofitClient.getInstance()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getUpcomingMovie() = apiService!!.getUpComingMovies()

    fun getLatestMovies() = apiService!!.getLatestMovies()

    fun getPopularMovies() = apiService!!.getPopularMovies()

    fun getMoreUpcomingMovies(pageNumber: Int) = apiService!!.getMoreUpcomingMovies(pageNumber)

    fun getMoreLatestMovies(pageNumber: Int) = apiService!!.getMoreLatestMovies(pageNumber)

    fun getMorePopularMovies(pageNumber: Int) = apiService!!.getMorePopularMovies(pageNumber)
}