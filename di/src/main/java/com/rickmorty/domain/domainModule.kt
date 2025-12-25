package com.rickmorty.domain

import com.rickmorty.core.dispatcher.DefaultDispatcher
import com.rickmorty.domain.usecase.character.GetCharactersUseCase
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val domainModule = module {
    factory { GetCharactersUseCase(get(qualifier<DefaultDispatcher>()),get()) }
}