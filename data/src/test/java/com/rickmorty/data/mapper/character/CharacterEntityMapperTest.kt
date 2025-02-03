package com.rickmorty.data.mapper.character

import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.network.character.dto.CharacterDTO
import com.rickmorty.domain.model.CharacterModel
import org.junit.Test

class CharacterEntityMapperTest {

    @Test
    fun should_mapAEntity_to_model() {
        // Arrange
        val dto = CharacterEntity(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        val expectedResult = CharacterModel(
            name = "Rick Sanchez",
            status = CharacterModel.Status.Alive,
            species = "Human",
            gender = CharacterModel.Gender.Male,
            originLocation = "Earth (C-137)",
            lastKnownLocation = "Citadel of Ricks",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        // Act
        val result = dto
            .toDomain()

        // Assert
        assert(result == expectedResult)
    }
}