package com.rickmorty.data.datasource.local.character.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "species")
    val species: String,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "origin")
    val origin: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "image")
    val image: String
) {
    companion object {
        const val TABLE_NAME = "characters"
        const val COLUMN_ID = "id"
    }
}