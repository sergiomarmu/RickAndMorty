package com.rickmorty.ui.feature.characterlist.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rickmorty.ui.utils.parcelable.character.CharacterUI

class CharacterDetailParameterProvider: PreviewParameterProvider<CharacterUI> {
    override val values = sequenceOf(
        CharacterUI(
            name = "Rick",
            status = CharacterUI.Status.Alive,
            species = "Human",
            gender = CharacterUI.Gender.Male,
            originLocation = "Earth (C-137)",
            lastKnownLocation = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )
    )
}