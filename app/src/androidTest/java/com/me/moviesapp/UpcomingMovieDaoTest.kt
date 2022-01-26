package com.me.moviesapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.me.moviesapp.MockDataUtil.Companion.mockUpcomingMovieList
import com.me.moviesapp.data.Dao.UpcomingMoviesDao
import com.me.moviesapp.data.Database.Database
import com.me.moviesapp.data.Database.UpcomingDatabase
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
import io.reactivex.schedulers.Schedulers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpcomingMovieDaoTest{
    lateinit var db: Database
    lateinit var upcomingDao: UpcomingMoviesDao

    @Before
    fun initDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, Database::class.java).build()
        upcomingDao = db.upcomingMovieDao()
    }


    @Test
    fun insertAndVerify() {
        val mockData = mockUpcomingMovieList()
        upcomingDao.insertAll(mockData)
        var movies =  upcomingDao.getAllTest()
        MatcherAssert.assertThat(movies[0].id, Is.`is`(1))
        MatcherAssert.assertThat(movies[0].title, Is.`is`("Sample title"))
        MatcherAssert.assertThat(movies[0].overview, Is.`is`("Sample Overview"))
        MatcherAssert.assertThat(movies[0].voteAvearge, Is.`is`(5.0f))
        MatcherAssert.assertThat(movies[0].posterPath, Is.`is`("Sample Path"))
        MatcherAssert.assertThat(movies[0].releaseDate, Is.`is`("Release Date"))
    }


    @After
    fun closeDB() {
        db.close()
    }


}