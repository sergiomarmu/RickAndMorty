package com.rickmorty.domain.repository

import androidx.paging.PagingData
import com.rickmorty.domain.model.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersFlow(): Flow<PagingData<CharacterModel>>
}