package com.me.moviesapp.data

import com.google.gson.annotations.SerializedName

data class MoviesModel(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("vote_average") val voteAverage: Int,
    @field:SerializedName("poster_path") val posterPath: String
)