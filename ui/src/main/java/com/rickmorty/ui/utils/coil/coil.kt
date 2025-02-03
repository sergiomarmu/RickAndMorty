package com.rickmorty.ui.utils.coil

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import java.io.File

// Coil configuration for caching images
fun buildImageLoaderWithCache(
    context: Context,
    cacheDir: File
) = ImageLoader(context)
    .newBuilder()
    .memoryCachePolicy(CachePolicy.ENABLED)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(context, 0.1)
            .strongReferencesEnabled(true)
            .build()
    }
    .diskCachePolicy(CachePolicy.ENABLED)
    .diskCache {
        DiskCache.Builder()
            .maxSizePercent(0.10)
            .directory(cacheDir)
            .build()
    }
    .logger(DebugLogger())
    .build()
