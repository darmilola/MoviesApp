package com.me.moviesapp.data

import com.google.gson.annotations.SerializedName

data class MoviesResult(
    @field:SerializedName("page") val currentPage: Int,
    @field:SerializedName("total_pages") val totalPage: Int,
    @field:SerializedName("results") val data: ArrayList<MoviesModel>
)