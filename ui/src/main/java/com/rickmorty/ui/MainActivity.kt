package com.rickmorty.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.SingletonImageLoader
import com.rickmorty.ui.feature.characterlist.CharacterListScreen
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.utils.coil.buildImageLoaderWithCache

class MainActivity : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Setup coil caching
        SingletonImageLoader.setSafe {
            buildImageLoaderWithCache(
                context = this,
                cacheDir = cacheDir
            )
        }

        setContent {
            RickMortyTheme {
                CharacterListScreen()
            }
        }
    }
}