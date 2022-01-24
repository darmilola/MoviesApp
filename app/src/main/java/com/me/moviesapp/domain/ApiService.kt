package com.me.moviesapp.domain

import com.me.moviesapp.data.MoviesModel
import com.me.moviesapp.data.MoviesResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("movie/upcoming?api_key=069577edacb3926634ef70a8fe6e91a0")
     fun getUpComingMovies(): Observable<MoviesResult?>

    @GET("movie/popular?api_key=069577edacb3926634ef70a8fe6e91a0")
     fun getPopularMovies(): Observable<MoviesResult?>

    @GET("movie/latest?api_key=069577edacb3926634ef70a8fe6e91a0")
     fun getLatestMovies(): Observable<MoviesModel?>

    @GET("movie/latest?api_key=069577edacb3926634ef70a8fe6e91a0")
    fun getMoreLatestMovies(@Query("page") page: Int): Observable<MoviesModel?>

    @GET("movie/upcoming?api_key=069577edacb3926634ef70a8fe6e91a0&page=")
    fun getMoreUpcomingMovies(@Query("page") page: Int): Observable<MoviesResult?>

    @GET("movie/popular?api_key=069577edacb3926634ef70a8fe6e91a0")
    fun getMorePopularMovies(@Query("page") page: Int): Observable<MoviesResult?>

}