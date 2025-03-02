package com.example.screenvibe.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color

import androidx.hilt.navigation.compose.hiltViewModel

import com.example.screenvibe.ui.viewmodel.MoviesViewModel

import com.example.screenvibe.data.models.QueryParams
import com.example.screenvibe.ui.components.GenreTabs
import com.example.screenvibe.ui.components.MovieGrid
import com.example.screenvibe.ui.components.TopBarWithSearch

@Composable
fun MoviesScreen(viewModel: MoviesViewModel = hiltViewModel()) {
    val genres by viewModel.genres.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val lazyGridState = rememberLazyGridState()


    LaunchedEffect(selectedGenre, searchQuery) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMoviesDebounced(searchQuery)
        } else {
            selectedGenre?.let { genre ->
                viewModel.discoverMovies(QueryParams.DiscoverMovie(withGenres = genre.id.toString()))
            }
        }
    }
    LaunchedEffect(selectedGenre) {
        lazyGridState.scrollToItem(0)
    }
    Column {
        TopBarWithSearch(
            query = searchQuery, onQueryChange = { searchQuery = it },
            selectedGenre = selectedGenre,
        )
        if (genres.isNotEmpty()) {
            GenreTabs(genres, selectedGenre) { viewModel.setSelectedGenre(it) }
        }
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier)
            }
        } else if (errorMessage != null) {
            Text("ERROR: $errorMessage", color = Color.Red, modifier = Modifier.fillMaxSize())
        } else {
            MovieGrid(
                movies = movies,
                onLoadMore = { viewModel.loadMoreMovies() },
                lazyGridState = lazyGridState
            )
        }
    }
}
