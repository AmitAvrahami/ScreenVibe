package com.example.screenvibe.data.models

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int = 0,
    val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double = 0.0,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double = 0.0,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("video") val hasVideo: Boolean = true,
    @SerializedName("adult") val isAdult: Boolean = true,
    @SerializedName("genre_ids") val genreIds: List<Int> = emptyList(),
    var posterFullPath: String? = null

) {

}