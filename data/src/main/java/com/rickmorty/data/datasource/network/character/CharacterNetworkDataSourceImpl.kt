package com.rickmorty.data.datasource.network.character

import com.rickmorty.data.datasource.network.character.api.RickAndMortyApi
import com.rickmorty.data.handler.tryRequest

class CharacterNetworkDataSourceImpl(
    private val api: RickAndMortyApi
) : CharacterNetworkDataSource {

    override suspend fun getCharactersWithPaging(
        page: Int
    ) = tryRequest { api.getCharacters(page) }

}
