package com.rickmorty.domain

import com.rickmorty.domain.usecase.character.GetCharactersUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetCharactersUseCase(get()) }
}