package com.rickmorty.ui.feature.characterlist

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.rickmorty.ui.utils.parcelable.character.CharacterUI

class CharacterListParameterProvider : CollectionPreviewParameterProvider<List<CharacterUI>>(
    listOf(
        listOf(
            CharacterUI(
                name = "Rick",
                status = CharacterUI.Status.Alive,
                species = "Human",
                gender = CharacterUI.Gender.Male,
                originLocation = "Earth (C-137)",
                lastKnownLocation = "Citadel of Ricks",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            ),
            CharacterUI(
                name = "Crocubot",
                status = CharacterUI.Status.Dead,
                species = "Alien",
                gender = CharacterUI.Gender.Male,
                originLocation = "unknown",
                lastKnownLocation = "Worldender's lair",
                image = "https://rickandmortyapi.com/api/character/avatar/81.jpeg"
            ),
            CharacterUI(
                name = "Armagheadon",
                status = CharacterUI.Status.Alive,
                species = "species",
                gender = CharacterUI.Gender.Male,
                originLocation = "Signus 5 Expanse",
                lastKnownLocation = "Signus 5 Expanse",
                image = "https://rickandmortyapi.com/api/character/avatar/24.jpeg"
            )
        )
    )
)