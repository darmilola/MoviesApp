package com.me.moviesapp.data.Dao

import androidx.room.*
import com.me.moviesapp.data.Entity.PopularMoviesEntity

@Dao
interface PopularMoviesDao {
    @Query("SELECT * FROM popularMovies")
    fun getAll(): List<PopularMoviesEntity>


    @Insert
    fun insertAll(vararg movies: PopularMoviesEntity)

}