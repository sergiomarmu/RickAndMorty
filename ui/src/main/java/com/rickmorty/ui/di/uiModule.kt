package com.rickmorty.ui.di

import com.rickmorty.ui.feature.characterlist.CharacterListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { CharacterListViewModel(get()) }
}