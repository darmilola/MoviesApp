package com.me.moviesapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.me.moviesapp.data.Dao.PopularMoviesDao
import com.me.moviesapp.data.Database.Database
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PopularMovieDaoTest {

    lateinit var db: Database
    lateinit var popularMoviesDao: PopularMoviesDao

    @Before
    fun initDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, Database::class.java).build()
        popularMoviesDao = db.popularMovieDao()
    }


    @Test
    fun insertAndVerify() {
        val mockData = MockDataUtil.mockPopularMovieList()
        popularMoviesDao.insertAll(mockData)
        var movies =  popularMoviesDao.getAllTest()
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