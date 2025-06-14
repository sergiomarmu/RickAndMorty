package com.rickmorty.data.datasource.network.character

import com.rickmorty.data.datasource.network.character.api.RickAndMortyApi
import com.rickmorty.data.handler.tryRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CharacterNetworkDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val api: RickAndMortyApi
) : CharacterNetworkDataSource {

    override suspend fun getCharactersWithPaging(
        page: Int
    ) = tryRequest(ioDispatcher= ioDispatcher) { api.getCharacters(page) }
}
