package com.rickmorty.application

import android.app.Application
import com.rickmorty.core.coreModule
import com.rickmorty.data.dataModule
import com.rickmorty.domain.domainModule
import com.rickmorty.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TheApplication)
            modules(
                listOf(
                    uiModule,
                    domainModule,
                    dataModule,
                    coreModule
                )
            )
        }
    }
}