package com.example.screenvibe.ui.viewmodel

import ConfigurationResponse
import com.example.screenvibe.data.models.Movie
import MovieDetailsResponse
import Review
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenvibe.data.models.*
import com.example.screenvibe.data.repositories.MoviesRepository
import com.example.screenvibe.utils.TmdbErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var _selectedGenre = MutableStateFlow<Genre?>(null)
    val selectedGenre = _selectedGenre.asStateFlow()

    private val _imageBaseUrl = MutableStateFlow<String?>(null)
    private val imageBaseUrl: StateFlow<String?> = _imageBaseUrl.asStateFlow()

    private val _configuration = MutableStateFlow<ConfigurationResponse?>(null)
    val configuration: StateFlow<ConfigurationResponse?> = _configuration.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres = flow {
        repository.getGenres(QueryParams.Genres())
            .onSuccess { emit(it.genres) }
            .onFailure { _errorMessage.value = it.localizedMessage ?: "Error fetching genres" }
    }
        .onEach { loadedGenres ->
            _genres.value = loadedGenres
            if (_selectedGenre.value == null && loadedGenres.isNotEmpty()) {
                _selectedGenre.value = loadedGenres.first()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var currentPage = 1
    private var canLoadMore = true

    private var searchJob: Job? = null

    init {
        fetchImageConfiguration()
    }

    fun searchMoviesDebounced(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            searchMovies(query)
        }
    }

    fun setSelectedGenre(genre: Genre) {
        _selectedGenre.value = genre
    }

    private  fun searchMovies(query: String) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null

        val params = QueryParams.SearchMovie(query = query, page = 1)
        repository.searchMovies(params)
            .onSuccess { result ->
                _movies.value = emptyList()
                _movies.value = result.results.map { movie ->
                    movie.copy(posterFullPath = imageBaseUrl.value?.let { "$it${movie.posterPath}" } ?: "")
                }
            }
            .onFailure {
                _errorMessage.value = when (it) {
                    is HttpException -> TmdbErrorHandler.getErrorMessage(it.code())
                    else -> it.localizedMessage ?: "Unknown error occurred."
                }
            }

        _isLoading.value = false
    }



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

    fun discoverMovies(params: QueryParams.DiscoverMovie = QueryParams.DiscoverMovie(page = 1)) {
        handleApiCall({ repository.getMoviesByParams(params) }) {
            _movies.value = it.results.map { movie ->
                movie.copy(posterFullPath = imageBaseUrl.value?.let { "$it${movie.posterPath}" } ?: "")
            }
            currentPage = 1
            canLoadMore = it.results.isNotEmpty()
        }
    }

    fun loadMoreMovies() {
        if (!canLoadMore || _isLoading.value) return
        val nextPage = currentPage + 1
        val params = QueryParams.DiscoverMovie(page = nextPage)

        handleApiCall({ repository.getMoviesByParams(params) }) { response ->
            if (response.results.isNotEmpty()) {
                _movies.value += response.results.map { movie ->
                    movie.copy(posterFullPath = imageBaseUrl.value?.let { "$it${movie.posterPath}" } ?: "")
                }
                currentPage = nextPage
            } else {
                canLoadMore = false
            }
        }
    }

    private fun fetchImageConfiguration() {
        viewModelScope.launch {
            repository.getConfiguration()
                .onSuccess { config ->
                    val baseUrl = config.images.secureBaseUrl
                    val size = config.images.posterSizes.find { it == "w500" } ?: "original"
                    _imageBaseUrl.value = "$baseUrl$size/"
                }
                .onFailure {
                    _errorMessage.value = it.localizedMessage ?: "Failed to fetch image configuration"
                }
        }
    }



    fun getGenres() {
        val params = QueryParams.Genres()
        handleApiCall({ repository.getGenres(params) }) { _genres.value = it.genres }
    }
}

