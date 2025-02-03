package com.rickmorty.data.datasource.network.character.dto.paging

import com.rickmorty.data.datasource.network.character.dto.CharacterDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterPagingWrapperDTO(
    @SerialName("info")
    val info: CharacterPagingInfoDTO,
    @SerialName("results")
    val results: List<CharacterDTO>
)