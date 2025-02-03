package com.rickmorty.data.datasource.local.character.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rickmorty.data.datasource.local.character.dao.CharacterDAO
import com.rickmorty.data.datasource.local.character.dao.CharacterPagingKeyDAO
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey

@Database(
    version = AppRoomDatabase.DATABASE_VERSION,
    entities = [
        CharacterEntity::class,
        CharacterRemoteKey::class
    ]
)
abstract class AppRoomDatabase : RoomDatabase() {
    companion object {
        internal const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "rick_and_morty_db"

        fun createInstance(
            applicationContext: Context,
        ) = Room
            .databaseBuilder(
                applicationContext,
                AppRoomDatabase::class.java,
                DATABASE_NAME
            )
            .build()
    }

    abstract fun characterDAO(): CharacterDAO
    abstract fun characterPagingKeyDAO(): CharacterPagingKeyDAO
}