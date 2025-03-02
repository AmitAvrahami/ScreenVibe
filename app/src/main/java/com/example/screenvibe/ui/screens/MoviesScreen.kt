package com.example.screenvibe.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.screenvibe.viewmodel.MoviesViewModel

@Composable
fun MovieScreen(viewModel: MoviesViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        errorMessage?.let {
            Text(text = "Error: $it", color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        Button(onClick = { viewModel.discoverMovies() }) {
            Text("Load Movies")
        }

        LazyColumn {
            items(movies) { movie ->
                Text(text = movie.title, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
