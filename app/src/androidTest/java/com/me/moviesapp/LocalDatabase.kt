package com.me.moviesapp

import androidx.room.Room
import com.me.moviesapp.data.Database.UpcomingDatabase
import com.me.moviesapp.presentation.MyApplication
import org.junit.After
import org.junit.Before

abstract class LocalDatabase {
    lateinit var db: UpcomingDatabase
    var context = MyApplication()

    @Before
    fun initDB() {
        db = Room.inMemoryDatabaseBuilder(context.getInstance()!!, UpcomingDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDB() {
        db.close()
    }
}