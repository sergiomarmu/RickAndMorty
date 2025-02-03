package com.rickmorty.data.datasource.network.character.api

import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingWrapperDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<CharacterPagingWrapperDTO>
}
