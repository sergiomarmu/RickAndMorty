package com.rickmorty.data.datasource.local.character.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey.Companion.TABLE_NAME

@Dao
interface CharacterPagingKeyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(
        key: CharacterRemoteKey
    )

    @Query("select * from $TABLE_NAME where id=:key")
    suspend fun getKeyById(
        key: String
    ): CharacterRemoteKey?

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()
}

const val KEY_ID = "paging_key_id"