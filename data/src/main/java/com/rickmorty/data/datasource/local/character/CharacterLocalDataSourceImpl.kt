package com.rickmorty.data.datasource.local.character

import com.rickmorty.data.datasource.local.character.db.AppRoomDatabase
import com.rickmorty.data.datasource.local.character.dao.CharacterDAO
import com.rickmorty.data.datasource.local.character.dao.CharacterPagingKeyDAO
import com.rickmorty.data.datasource.local.character.dao.KEY_ID
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey

class CharacterLocalDataSourceImpl(
    override val db: AppRoomDatabase,
    private val characterDAO: CharacterDAO,
    private val characterPagingKeyDAO: CharacterPagingKeyDAO
) : CharacterLocalDataSource {

    override suspend fun upsertAllCharacters(
        characters: List<CharacterEntity>
    ) = characterDAO.upsertAll(characters)

    override fun readAllCharacters() = characterDAO.readAll()

    override suspend fun deleteAllCharacters() = characterDAO.deleteAll()

    override suspend fun insertKey(
        key: CharacterRemoteKey
    ) = characterPagingKeyDAO.insertKey(key)

    override suspend fun getKeyById(
        key: String
    ) = characterPagingKeyDAO.getKeyById(KEY_ID)

    override suspend fun deleteAllKeys() = characterPagingKeyDAO.deleteAll()
}
