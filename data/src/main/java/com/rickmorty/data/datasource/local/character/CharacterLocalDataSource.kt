package com.rickmorty.data.datasource.local.character

import androidx.paging.PagingSource
import com.rickmorty.data.datasource.local.character.db.AppRoomDatabase
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey

interface CharacterLocalDataSource {

    val db: AppRoomDatabase

    suspend fun upsertAllCharacters(
        characters: List<CharacterEntity>
    )

    fun readAllCharacters(): PagingSource<Int, CharacterEntity>

    suspend fun deleteAllCharacters()

    suspend fun insertKey(
        key: CharacterRemoteKey
    )

    suspend fun getKeyById(
        key: String
    ): CharacterRemoteKey?

    suspend fun deleteAllKeys()
}
