package com.example.screenvibe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.screenvibe.data.models.Genre

@Composable
fun GenreTabs(
    genres: List<Genre>,
    selectedGenre: Genre?,
    onGenreSelected: (Genre) -> Unit
) {
    val selectedIndex = genres.indexOf(selectedGenre).takeIf { it >= 0 } ?: 0

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 8.dp,
        containerColor = MaterialTheme.colorScheme.surface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        genres.forEachIndexed { index, genre ->
            val isSelected = genre == selectedGenre
            val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f, label = "Tab Scale")
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                label = "Tab Background Color"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                label = "Tab Text Color"
            )

            Tab(
                selected = isSelected,
                onClick = { onGenreSelected(genre) },
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = { Text(genre.name, color = textColor) }
            )
        }
    }
}