package com.me.moviesapp.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class MoviesModel(
    @field:SerializedName ("id") val id: Int,
    @field:SerializedName("title") val title: String?,
    @field:SerializedName("overview") val overview: String?,
    @field:SerializedName("vote_average") val voteAverage: Float,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("release_date") val releaseDate: String?
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeFloat(voteAverage)
        parcel.writeString(posterPath)
        parcel.writeString(releaseDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoviesModel> {
        override fun createFromParcel(parcel: Parcel): MoviesModel {
            return MoviesModel(parcel)
        }

        override fun newArray(size: Int): Array<MoviesModel?> {
            return arrayOfNulls(size)
        }
    }
}