package com.me.moviesapp.domain

import com.me.moviesapp.data.MoviesResult
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

abstract class ApiService {

    @GET("/movie/upcoming")
    abstract fun getUpComingMovies(): Observable<MoviesResult?>

    @GET("/movie/popular")
    abstract fun getPopularMovies(): Observable<MoviesResult?>

    @GET("/movie/latest")
    abstract fun getLatestMovies(): Observable<MoviesResult?>
}