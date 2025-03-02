package com.example.screenvibe.viewmodel

import ConfigurationResponse
import Movie
import MovieDetailsResponse
import Review
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenvibe.data.models.*
import com.example.screenvibe.data.repositories.MoviesRepository
import com.example.screenvibe.utils.TmdbErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _movieDetails = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetails: StateFlow<MovieDetailsResponse?> = _movieDetails.asStateFlow()

    private val _movieReviews = MutableStateFlow<List<Review>>(emptyList())
    val movieReviews: StateFlow<List<Review>> = _movieReviews.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _configuration = MutableStateFlow<ConfigurationResponse?>(null)
    val configuration: StateFlow<ConfigurationResponse?> = _configuration.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private fun <T> handleApiCall(
        apiCall: suspend () -> Result<T>,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            apiCall()
                .onSuccess {
                    _isLoading.value = false
                    onSuccess(it)
                }
                .onFailure { throwable ->
                    _isLoading.value = false
                    _errorMessage.value = when (throwable) {
                        is HttpException -> TmdbErrorHandler.getErrorMessage(throwable.code())
                        else -> throwable.localizedMessage ?: "Unknown error occurred."
                    }
                }
        }
    }

    fun discoverMovies(params: QueryParams.DiscoverMovie = QueryParams.DiscoverMovie()) {
        handleApiCall({ repository.getMoviesByParams(params) }) { _movies.value = it.results }
    }

    fun searchMovies(query: String) {
        val params = QueryParams.SearchMovie(query = query, page = 1)
        handleApiCall({ repository.searchMovies(params) }) { _movies.value = it.results }
    }

    fun getMovieDetails(movieId: Int) {
        val params = QueryParams.MovieDetails(appendToResponse = "videos,credits")
        handleApiCall({ repository.getMovieDetails(movieId, params) }) { _movieDetails.value = it }
    }

    fun getMovieReviews(movieId: Int) {
        val params = QueryParams.MovieReviews(page = 1)
        handleApiCall({ repository.getMovieReviews(movieId, params) }) { _movieReviews.value = it.reviews }
    }

    fun getGenres() {
        val params = QueryParams.Genres()
        handleApiCall({ repository.getGenres(params) }) { _genres.value = it.genres }
    }

    fun getConfiguration() {
        handleApiCall({ repository.getConfiguration() }) { _configuration.value = it }
    }
}