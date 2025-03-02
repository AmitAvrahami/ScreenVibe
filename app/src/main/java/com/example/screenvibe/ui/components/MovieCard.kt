package com.example.screenvibe.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.screenvibe.data.models.Movie


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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "👆🏻Touch to expand",
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