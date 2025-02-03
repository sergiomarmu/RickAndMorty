package com.rickmorty.data.datasource.network.character.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDTO(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("status")
    val status: String,
    @SerialName("species")
    val species: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("origin")
    val origin: Origin,
    @SerialName("location")
    val location: Location,
    @SerialName("image")
    val image: String
) {
    @Serializable
    data class Origin(
        @SerialName("name")
        val name: String
    )

    @Serializable
    data class Location(
        @SerialName("name")
        val name: String
    )
}