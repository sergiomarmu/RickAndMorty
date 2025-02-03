package com.rickmorty.ui.utils.parcelable.character

import com.rickmorty.domain.model.CharacterModel
import org.junit.Test

class CharacterUIMapperTest {

    @Test
    fun should_mapAModelEntity_to_uIEntity() {
        // Arrange
        val model = CharacterModel(
            name = "Rick Sanchez",
            status = CharacterModel.Status.Alive,
            species = "Human",
            gender = CharacterModel.Gender.Male,
            originLocation = "Earth (C-137)",
            lastKnownLocation = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        val expectedResult = CharacterUI(
            name = "Rick Sanchez",
            status = CharacterUI.Status.Alive,
            species = "Human",
            gender = CharacterUI.Gender.Male,
            originLocation = "Earth (C-137)",
            lastKnownLocation = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        // Act
        val result = model
            .toCharacterUI()

        // Assert
        assert(result == expectedResult)
    }
}