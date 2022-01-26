package com.me.moviesapp.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.me.moviesapp.data.Dao.LatestMovieDao
import com.me.moviesapp.data.Dao.PopularMoviesDao
import com.me.moviesapp.data.Dao.UpcomingMoviesDao
import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity

@Database(entities = [PopularMoviesEntity::class], version = 1)
abstract class PopularDatabase : RoomDatabase() {
    abstract fun popularMoviesDao(): PopularMoviesDao


    companion object {
        private const val DB_NAME = "popularMovies"

        @Volatile
        private var instance: PopularDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PopularDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

}