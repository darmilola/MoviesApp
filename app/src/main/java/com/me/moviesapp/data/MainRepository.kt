package com.me.moviesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.me.moviesapp.data.Database.LatestDatabase
import com.me.moviesapp.data.Database.PopularDatabase
import com.me.moviesapp.data.Database.UpcomingDatabase
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.Observable

class MainRepository(private val apiHelper: ApiHelper) {

    fun getRemoteLatestMovies(): Observable<MoviesModel?> {
        return apiHelper.getLatestMovies()
    }

    fun getRemotePopularMovies(): Observable<MoviesResult?> {
        return apiHelper.getPopularMovies()
    }

    fun getRemoteUpcomingMovies(): Observable<MoviesResult?> {
        return apiHelper.getUpcomingMovie()
    }

    fun getMoreRemoteUpcomingMovies(pageNumber: Int): Observable<MoviesResult?> {
        return apiHelper.getMoreUpcomingMovies(pageNumber)
    }

    fun getMoreRemoteLatestMovies(pageNumber: Int): Observable<MoviesModel?> {
        return apiHelper.getMoreLatestMovies(pageNumber)
    }

    fun getMoreRemotePopularMovies(pageNumber: Int): Observable<MoviesResult?> {
        return apiHelper.getMorePopularMovies(pageNumber)
    }

    fun getLocalUpcomingMovies(context: Context): Observable<List<UpcomingMoviesEntity>> {

        val db = Room.databaseBuilder(
            context,
            com.me.moviesapp.data.Database.Database::class.java, "upcomingMovies"
        ).build()
        return db.upcomingMovieDao().getAll()
    }

    fun getLocalLatestMovies(context: Context): Observable<List<LatestMoviesEntity>> {

        val db = Room.databaseBuilder(
            context,
            com.me.moviesapp.data.Database.Database::class.java, "latestMovies"
        ).build()

        return db.latestMovieDao().getAll()
    }

    fun getLocalPopularMovies(context: Context): Observable<List<PopularMoviesEntity>> {

        val db = Room.databaseBuilder(
            context,
            com.me.moviesapp.data.Database.Database::class.java, "popularMovies"
        ).build()

        return db.popularMovieDao().getAll()
    }

}