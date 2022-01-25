package com.me.moviesapp.data.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upcomingMovies")
data class UpcomingMoviesEntity(@PrimaryKey @ColumnInfo(name = "id") var id: Int, @ColumnInfo(name = "title") var title: String?,
                                @ColumnInfo(name = "overview") var overview: String?, @ColumnInfo(name = "vote_average") var voteAvearge: Float,
                                @ColumnInfo(name = "poster_path") var posterPath: String?, @ColumnInfo(name = "release_date") var releaseDate: String?
) {


}