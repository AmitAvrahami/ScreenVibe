package com.example.screenvibe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.screenvibe.data.models.Genre

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
