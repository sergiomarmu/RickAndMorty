package com.rickmorty.data.repository

import androidx.paging.Pager
import androidx.paging.map
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.mapper.character.toDomain
import com.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val pager: Pager<Int, CharacterEntity>
) : CharacterRepository {

    override fun getCharactersFlow() = pager
        .flow
        .map { pagingData ->
            pagingData
                .map { characterEntity ->
                    characterEntity.toDomain()
                }
        }
}

