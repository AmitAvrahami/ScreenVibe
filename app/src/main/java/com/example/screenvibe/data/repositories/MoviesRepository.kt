package com.example.screenvibe.data.repositories

import ConfigurationResponse
import MovieDetailsResponse
import ReviewResponse
import com.example.screenvibe.data.api.TmdbApiService
import com.example.screenvibe.data.models.GenresResponse
import com.example.screenvibe.data.models.MovieResponse
import com.example.screenvibe.data.models.QueryParams
import jakarta.inject.Inject
import jakarta.inject.Singleton

class MoviesRepository @Inject constructor(
    private val apiService: TmdbApiService
) {


    suspend fun getMoviesByParams(params: QueryParams.DiscoverMovie): Result<MovieResponse> {
        return runCatching { apiService.getMovies(params.toMap()) }
    }

    suspend fun searchMovies(params: QueryParams.SearchMovie): Result<MovieResponse> {
        return runCatching { apiService.searchMovies(params.toMap()) }
    }


    suspend fun getGenres(params: QueryParams.Genres = QueryParams.Genres()): Result<GenresResponse> {
        return runCatching { apiService.getGenres(params.toMap()) }
    }

    suspend fun getConfiguration(): Result<ConfigurationResponse> {
        return runCatching { apiService.getConfiguration(QueryParams.Configuration.toMap()) }
    }
}