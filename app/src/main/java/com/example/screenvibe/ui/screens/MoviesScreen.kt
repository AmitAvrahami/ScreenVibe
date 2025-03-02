package com.example.screenvibe.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.screenvibe.R
import com.example.screenvibe.viewmodel.MoviesViewModel
import com.example.screenvibe.data.models.Genre
import com.example.screenvibe.data.models.Movie
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.screenvibe.data.models.QueryParams

@Composable
fun MoviesScreen(viewModel: MoviesViewModel = hiltViewModel()) {
    val genres by viewModel.genres.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf<Genre?>(null) }
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) { viewModel.getGenres() }
    LaunchedEffect(genres) {
        if (selectedGenre == null && genres.isNotEmpty()) {
            selectedGenre = genres.first()
        }
    }
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
            GenreTabs(genres, selectedGenre) { selectedGenre = it }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithSearch(query: String, onQueryChange: (String) -> Unit,selectedGenre: Genre?) {
    val topAppBarText = selectedGenre?.name ?: "Enjoy your movie"
    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        TopAppBar(title = { Text("🎬${topAppBarText}", color = Color.Black, fontWeight = FontWeight.Bold) })
        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            placeholder = { Text("Search Movie") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "search") }
        )
    }
}

@Composable
fun GenreTabs(genres: List<Genre>, selectedGenre: Genre?, onGenreSelected: (Genre) -> Unit) {
    val selectedIndex = genres.indexOf(selectedGenre).takeIf { it >= 0 } ?: 0

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 8.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(4.dp)
            )
        }
    ) {
        genres.forEachIndexed { index, genre ->
            val isSelected = genre == selectedGenre
            val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f, label = "Tab Scale")
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "Tab Background Color"
            )

            Tab(
                selected = isSelected,
                onClick = { onGenreSelected(genre) },
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .background(backgroundColor, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = { Text(genre.name, color = if (isSelected) Color.White else Color.Black) }
            )
        }
    }
}

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

@Composable
fun MovieCard(movie: Movie, isExpanded: Boolean, onClick: () -> Unit) {
    val transition = updateTransition(targetState = isExpanded, label = "Card Flip Transition")

    val yRotation by transition.animateFloat(
        label = "Rotation",
        transitionSpec = { tween(durationMillis = 600, easing = FastOutSlowInEasing) }
    ) { expanded -> if (expanded) 360f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.66f)
            .graphicsLayer {
                rotationY = yRotation
            }
            .clickable { onClick() }
    ) {
        if (yRotation > 90f) {
            MovieDetailsCard(movie)
        } else {
            MoviePosterCard(movie)
        }
    }
}

@Composable
fun MoviePosterCard(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = movie.posterFullPath,
                contentDescription = "poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📅 ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
@Composable
fun MovieDetailsCard(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(movie.posterFullPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "📅 Release: ${movie.releaseDate}", color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "⭐ Vote: ${movie.voteAverage}/10", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "📖 Overview:\n${movie.overview}", color = Color.White)
            }
        }
    }
}