package com.rickmorty.ui.feature.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.rickmorty.domain.usecase.character.GetCharactersUseCase
import com.rickmorty.ui.utils.parcelable.character.toCharacterUI
import kotlinx.coroutines.flow.map

class CharacterListViewModel(
    useCase: GetCharactersUseCase
) : ViewModel() {

    // [cachedIn], caches the paged data in memory within the ViewModel scope for reuse
    val state = useCase
        .invoke()
        .map { pagingData ->
            pagingData
                .map { characterModel ->
                    characterModel.toCharacterUI()
                }
        }
        .cachedIn(viewModelScope)
}