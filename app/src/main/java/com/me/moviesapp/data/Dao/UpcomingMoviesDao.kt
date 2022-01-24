package com.me.moviesapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.Observable

interface UpcomingMoviesDao {

        @Query("SELECT * FROM upcomingMovies")
        fun getAll(): Observable<List<UpcomingMoviesEntity>>


        @Insert
        fun insertAll(movies: List<UpcomingMoviesEntity>)


}