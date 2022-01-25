package com.me.moviesapp.Api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.me.moviesapp.data.ApiHelper
import com.me.moviesapp.data.MoviesResult
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.apache.commons.io.IOUtils
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import com.google.gson.GsonBuilder

import com.google.gson.Gson
import com.me.moviesapp.RxImmediateSchedulerRule
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.stream.Collectors


class MockUpcomingMovies
{
    @Rule
    @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val ASSET_BASE_PATH = "../app/src/main/assets/"
    private lateinit var mockWebServer: MockWebServer
    private var apiHelper = ApiHelper()
    private lateinit var moviesResult: MoviesResult

    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @Throws(IOException::class)
    @Before
    fun mockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Throws(IOException::class)
    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun upcomingMoviesTest() {

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(readJsonFile("movies_response.json"))
            .setResponseCode(200)
             mockWebServer.enqueue(response)


             apiHelper.getUpcomingMovie()
                 .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ moviesResult: MoviesResult? -> TestServerResponse(moviesResult,response)

                       }, {t: Throwable -> print(t)

            })
        print(response.body.toString())
        assertTrue(response.body.toString().contains("page"))

    }

    private fun TestServerResponse(moviesResult: MoviesResult?, response: MockResponse){
       // assertTrue(response.toString().contains("page"))
        assertTrue(moviesResult!!.currentPage == 4)
        assertTrue(moviesResult!!.data != null)
    }


    fun readJsonFile(filename: String): String? {
        var br =
            BufferedReader(InputStreamReader(FileInputStream(ASSET_BASE_PATH + filename)))
        val json: String = br.lines()
            .parallel()
            .collect(Collectors.joining())
        return json
    }

}