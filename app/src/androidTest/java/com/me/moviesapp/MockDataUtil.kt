package com.me.moviesapp

import com.me.moviesapp.data.Entity.LatestMoviesEntity
import com.me.moviesapp.data.Entity.PopularMoviesEntity
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity

class MockDataUtil {
    companion object {

        fun mockUpcomingMovieList(): List<UpcomingMoviesEntity>{
            var movies = ArrayList<UpcomingMoviesEntity>()
            var upcomingMoviesEntity = UpcomingMoviesEntity(
                1,
                "Sample title",
                "Sample Overview",
                5.0f,
                "Sample Path",
                "Release Date"
            )

            movies.add(upcomingMoviesEntity)
            movies.add(upcomingMoviesEntity)
            movies.add(upcomingMoviesEntity)
            movies.add(upcomingMoviesEntity)
            movies.add(upcomingMoviesEntity)

            return movies;
        }

        fun mockLatestMovieList(): ArrayList<LatestMoviesEntity>{
            var movies = ArrayList<LatestMoviesEntity>()
            var latestMoviesEntity = LatestMoviesEntity(
                1,
                "Sample title",
                "Sample Overview",
                5.0f,
                "Sample Path",
                "Release Date"
            )

            movies.add(latestMoviesEntity)
            movies.add(latestMoviesEntity)
            movies.add(latestMoviesEntity)
            movies.add(latestMoviesEntity)
            movies.add(latestMoviesEntity)
            movies.add(latestMoviesEntity)


            return movies;
        }

        fun mockPopularMovieList(): ArrayList<PopularMoviesEntity>{
            var movies = ArrayList<PopularMoviesEntity>()
            var popularMoviesEntity = PopularMoviesEntity(
                1,
                "Sample title",
                "Sample Overview",
                5.0f,
                "Sample Path",
                "Release Date"
            )

            movies.add(popularMoviesEntity)
            movies.add(popularMoviesEntity)
            movies.add(popularMoviesEntity)
            movies.add(popularMoviesEntity)
            movies.add(popularMoviesEntity)
            movies.add(popularMoviesEntity)

            return movies;
        }

    }
}