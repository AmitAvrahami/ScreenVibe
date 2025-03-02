package com.example.screenvibe.data.api

import ConfigurationResponse
import MovieDetailsResponse
import ReviewResponse
import com.example.screenvibe.data.models.GenresResponse
import com.example.screenvibe.data.models.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TmdbApiService {

    @GET("/3/genre/movie/list")
    suspend fun getGenres(@QueryMap options: Map<String, String>): GenresResponse

    @GET("/3/discover/movie")
    suspend fun getMovies(@QueryMap options: Map<String, String>): MovieResponse

    @GET("/3/search/movie")
    suspend fun searchMovies(@QueryMap options: Map<String, String>): MovieResponse

    @GET("/3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @QueryMap options: Map<String, String>
    ): MovieDetailsResponse

    @GET("/3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @QueryMap options: Map<String, String>
    ): ReviewResponse

    @GET("/3/configuration")
    suspend fun getConfiguration(
        @QueryMap options: Map<String, String>
    ): ConfigurationResponse
}

