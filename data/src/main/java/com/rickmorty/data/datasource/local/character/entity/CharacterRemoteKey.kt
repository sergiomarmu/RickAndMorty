package com.rickmorty.data.datasource.local.character.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class CharacterRemoteKey(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    val nextPage: Int?
) {
    companion object {
        const val TABLE_NAME = "character_remote_keys"
        const val COLUMN_ID = "id"
    }
}