package com.example.screenvibe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ScreenVibeTheme
import com.example.screenvibe.data.models.MovieResponse
import com.example.screenvibe.data.models.QueryParams
import com.example.screenvibe.ui.MoviesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScreenVibeTheme {
                MoviesScreen()
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ScreenVibeTheme {
        }
    }
}
