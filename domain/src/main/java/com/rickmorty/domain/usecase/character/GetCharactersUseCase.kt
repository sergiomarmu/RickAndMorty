package com.rickmorty.domain.usecase.character

import com.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class GetCharactersUseCase(
    private val defaultDispatcher: CoroutineDispatcher,
    private val repository: CharacterRepository
) {
    operator fun invoke() = repository
        .getCharactersFlow()
        .distinctUntilChanged()
        .flowOn(defaultDispatcher)
}