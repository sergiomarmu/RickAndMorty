package com.rickmorty.data.datasource.local.character.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity.Companion.TABLE_NAME

@Dao
interface CharacterDAO {
    @Upsert
    suspend fun upsertAll(
        characters: List<CharacterEntity>
    )

    @Query("SELECT * FROM $TABLE_NAME")
    fun readAll(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}