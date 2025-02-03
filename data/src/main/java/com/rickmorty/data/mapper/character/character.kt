package com.rickmorty.data.mapper.character

import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.network.character.dto.CharacterDTO
import com.rickmorty.domain.model.CharacterModel

fun CharacterDTO.toDomain() = CharacterModel(
    name = this.name,
    status = when (this.status) {
        "Alive" -> CharacterModel.Status.Alive
        "Dead" -> CharacterModel.Status.Dead
        "unknown" -> CharacterModel.Status.Unknown
        else -> CharacterModel.Status.Unknown
    },
    species = this.species,
    gender = when (this.gender) {
        "Female" -> CharacterModel.Gender.Female
        "Male" -> CharacterModel.Gender.Male
        "Genderless" -> CharacterModel.Gender.Genderless
        "unknown" -> CharacterModel.Gender.Unknown
        else -> CharacterModel.Gender.Unknown
    },
    originLocation = this.origin.name,
    lastKnownLocation = this.location.name,
    image = this.image
)

fun CharacterDTO.toEntity() = CharacterEntity(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    gender = this.gender,
    origin = this.origin.name,
    location = this.location.name,
    image = this.image
)

fun CharacterEntity.toDomain() = CharacterModel(
    name = this.name,
    status = when (this.status) {
        "Alive" -> CharacterModel.Status.Alive
        "Dead" -> CharacterModel.Status.Dead
        "unknown" -> CharacterModel.Status.Unknown
        else -> CharacterModel.Status.Unknown
    },
    species = this.species,
    gender = when (this.gender) {
        "Female" -> CharacterModel.Gender.Female
        "Male" -> CharacterModel.Gender.Male
        "Genderless" -> CharacterModel.Gender.Genderless
        "unknown" -> CharacterModel.Gender.Unknown
        else -> CharacterModel.Gender.Unknown
    },
    originLocation = this.origin,
    lastKnownLocation = this.location,
    image = this.image
)