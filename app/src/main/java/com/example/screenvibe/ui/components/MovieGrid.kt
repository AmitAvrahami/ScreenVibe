package com.example.screenvibe.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.screenvibe.data.models.Movie


@Composable
fun MovieGrid(
    movies: List<Movie>,
    onLoadMore: () -> Unit,
    lazyGridState: LazyGridState
) {
    val expandedMovie = remember { mutableStateOf<Movie?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(4.dp),
        state = lazyGridState
    ) {
        items(movies) { movie ->

            MovieCard(
                movie = movie,
                isExpanded = expandedMovie.value == movie,
                onClick = { expandedMovie.value = if (expandedMovie.value == movie) null else movie }
            )
        }
        item {
            LaunchedEffect(lazyGridState) {
                snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastIndex ->
                        if (lastIndex == movies.size - 1) {
                            onLoadMore()
                        }
                    }
            }
        }
    }
}