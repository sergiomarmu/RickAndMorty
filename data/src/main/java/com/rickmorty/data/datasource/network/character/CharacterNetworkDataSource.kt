package com.rickmorty.data.datasource.network.character

import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingWrapperDTO

interface CharacterNetworkDataSource {
    suspend fun getCharactersWithPaging(
        page:Int
    ): CharacterPagingWrapperDTO
}
