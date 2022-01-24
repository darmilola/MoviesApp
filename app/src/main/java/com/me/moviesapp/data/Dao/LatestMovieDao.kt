package com.me.moviesapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity

interface LatestMovieDao {

        @Query("SELECT * FROM latestMovies")
        fun getAll(): List<LatestMoviesEntity>


        @Insert
        fun insertAll(vararg movies: LatestMoviesEntity)
}