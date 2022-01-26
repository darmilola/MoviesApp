package com.me.moviesapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.Observable

@Dao
interface LatestMovieDao {

        @Query("SELECT * FROM latestMovies")
        fun getAll(): Observable<List<LatestMoviesEntity>>

        @Query("SELECT * FROM latestMovies")
        fun getAllTest(): List<LatestMoviesEntity>


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(movies: List<LatestMoviesEntity>)
}