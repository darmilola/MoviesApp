package com.me.moviesapp.data.Dao

import androidx.room.*
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.Observable

@Dao
interface PopularMoviesDao {
    @Query("SELECT * FROM popularMovies")
    fun getAll(): Observable<List<PopularMoviesEntity>>

    @Query("SELECT * FROM popularMovies")
    fun getAllTest(): List<PopularMoviesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<PopularMoviesEntity>)

}