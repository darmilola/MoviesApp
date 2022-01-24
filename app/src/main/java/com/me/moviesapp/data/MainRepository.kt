package com.me.moviesapp.data

import android.content.Context
import androidx.room.Room
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.Observable
import kotlinx.coroutines.*

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

    fun getLocalUpcomingMovies(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.Default) {
                performGetRequest(context)
            }
        }
    }


    fun performGetRequest(context: Context) : Observable<List<UpcomingMoviesEntity>> {
        var db: Database = Room.databaseBuilder(
            context,
            Database::class.java, "upcomingMovies"
        ).build()

        var upcomingMoviesEntity =  db.upcomingMovieDao().getAll()
        return upcomingMoviesEntity
    }


}