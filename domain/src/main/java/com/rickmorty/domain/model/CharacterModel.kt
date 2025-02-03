package com.rickmorty.domain.model

data class CharacterModel(
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val originLocation: String,
    val lastKnownLocation: String,
    val image: String
) {
    sealed class Status {
        data object Alive : Status()
        data object Dead : Status()
        data object Unknown : Status()
    }

    sealed class Gender {
        data object Female : Gender()
        data object Male : Gender()
        data object Genderless : Gender()
        data object Unknown : Gender()
    }
}