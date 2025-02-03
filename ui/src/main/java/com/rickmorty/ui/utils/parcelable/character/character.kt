package com.rickmorty.ui.utils.parcelable.character

import android.os.Parcelable
import com.rickmorty.domain.model.CharacterModel
import com.rickmorty.domain.model.CharacterModel.Gender
import com.rickmorty.domain.model.CharacterModel.Status
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterUI(
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val originLocation: String,
    val lastKnownLocation: String,
    val image: String
) : Parcelable {

    sealed class Status : Parcelable {
        @Parcelize
        data object Alive : Status()

        @Parcelize
        data object Dead : Status()

        @Parcelize
        data object Unknown : Status()
    }

    sealed class Gender : Parcelable {
        @Parcelize
        data object Female : Gender()

        @Parcelize
        data object Male : Gender()

        @Parcelize
        data object Genderless : Gender()

        @Parcelize
        data object Unknown : Gender()
    }
}

fun CharacterModel.toCharacterUI() = CharacterUI(
    name = this.name,
    status = when (status) {
        Status.Alive -> CharacterUI.Status.Alive
        Status.Dead -> CharacterUI.Status.Dead
        Status.Unknown -> CharacterUI.Status.Unknown
    },
    species = this.species,
    gender = when (this.gender) {
        Gender.Female -> CharacterUI.Gender.Female
        Gender.Genderless -> CharacterUI.Gender.Genderless
        Gender.Male -> CharacterUI.Gender.Male
        Gender.Unknown -> CharacterUI.Gender.Unknown
    },
    originLocation = this.originLocation,
    lastKnownLocation = this.lastKnownLocation,
    image = this.image
)