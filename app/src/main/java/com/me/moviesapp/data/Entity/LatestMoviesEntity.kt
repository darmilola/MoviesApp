package com.me.moviesapp.data.Entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "latestMovies")
data class LatestMoviesEntity (@ColumnInfo(name = "id") var id: Int, @ColumnInfo(name = "title") var title: String,
                               @ColumnInfo(name = "overview") var overview: String, @ColumnInfo(name = "vote_average") var voteAvearge: Int,
                               @ColumnInfo(name = "poster_path") var posterPath: String, @ColumnInfo(name = "release_date") var releaseDate: String
) {


}