package com.rickmorty.core

import com.rickmorty.core.dispatcher.DefaultDispatcher
import com.rickmorty.core.dispatcher.IODispatcher
import com.rickmorty.core.dispatcher.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val coreModule = module {
    single<CoroutineDispatcher>(qualifier<DefaultDispatcher>()) { Dispatchers.Default }
    single<CoroutineDispatcher>(qualifier<MainDispatcher>()) { Dispatchers.Main }
    single<CoroutineDispatcher>(qualifier<IODispatcher>()) { Dispatchers.IO }
}