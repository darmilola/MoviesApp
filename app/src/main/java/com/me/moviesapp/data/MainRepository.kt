package com.me.moviesapp.data

import io.reactivex.Observable

class MainRepository(private val apiHelper: ApiHelper) {

    fun getRemoteLatestMovies(): Observable<MoviesResult?>{
        return apiHelper.getLatestMovies()
    }

    fun getRemotePopularMovies(): Observable<MoviesResult?>{
        return apiHelper.getPopularMovies()
    }

    fun getRemoteUpcomingMovies(): Observable<MoviesResult?>{
        return apiHelper.getUpcomingMovie()
    }

}