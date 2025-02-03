package com.rickmorty.data.utils

class JsonResourceReader {

    fun fromPath(
        path: String
    ): String {
        val resource = this.javaClass.classLoader
            ?.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Resource not found: $path")

        return resource.bufferedReader()
            .use { it.readText() }
    }
}