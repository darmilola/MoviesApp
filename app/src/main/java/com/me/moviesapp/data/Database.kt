package com.me.moviesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.me.moviesapp.data.Dao.LatestMovieDao
import com.me.moviesapp.data.Dao.PopularMoviesDao
import com.me.moviesapp.data.Dao.UpcomingMoviesDao
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity

@Database(entities = [LatestMoviesEntity::class,PopularMoviesEntity::class,UpcomingMoviesEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun latestMovieDao(): LatestMovieDao
    abstract fun popularMovieDao(): PopularMoviesDao
    abstract fun upcomingMovieDao(): UpcomingMoviesDao
}