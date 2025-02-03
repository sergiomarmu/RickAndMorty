package com.rickmorty.domain.usecase.character

import com.rickmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.distinctUntilChanged

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    operator fun invoke() = repository
        .getCharactersFlow()
        .distinctUntilChanged()
}