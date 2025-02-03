@file:OptIn(ExperimentalPagingApi::class)

package com.rickmorty.data

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rickmorty.data.datasource.local.character.CharacterLocalDataSource
import com.rickmorty.data.datasource.local.character.CharacterLocalDataSourceImpl
import com.rickmorty.data.datasource.local.character.db.AppRoomDatabase
import com.rickmorty.data.datasource.network.character.CharacterNetworkDataSource
import com.rickmorty.data.datasource.network.character.CharacterNetworkDataSourceImpl
import com.rickmorty.data.datasource.network.character.api.RickAndMortyApi
import com.rickmorty.data.mediator.CharacterRemoteMediator
import com.rickmorty.data.repository.CharacterRepositoryImpl
import com.rickmorty.domain.repository.CharacterRepository
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

private const val API_BASE_URL = "https://rickandmortyapi.com/api/"

val dataModule = module {
    single { Json { ignoreUnknownKeys = true } }

    // region OKHttp / Retrofit
    single { provideCacheInterceptor() }
    single { provideHttpLoggingInterceptor() }
    single { provideOkkHttpClient(get(), get(), get()) }
    single { provideRetrofit(get(), get()) }
    // endregion

    // region Room
    single { provideAppRoomDatabase(get()) }
    single { provideCharacterDAODatabase(get()) }
    single { provideCharacterPagingKeyDAODatabase(get()) }
    // endregion

    // region Api
    single { providePriceNetworkApi(get()) }
    // endregion

    // region Source
    single<CharacterLocalDataSource> { CharacterLocalDataSourceImpl(get(), get(), get()) }
    single<CharacterNetworkDataSource> { CharacterNetworkDataSourceImpl(get()) }
    // endregion

    // region Pager
    single { provideCharacterPager(get(), get()) }
    // endregion

    // region Repository
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    // endregion
}

// region Network

// region Retrofit
private fun provideCacheInterceptor() = Interceptor { chain ->
    val response: okhttp3.Response = chain.proceed(chain.request())
    val cacheControl = CacheControl.Builder()
        .maxAge(1, TimeUnit.HOURS)
        .build()
    response.newBuilder()
        .header("Cache-Control", cacheControl.toString())
        .build()
}

private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

private fun provideOkkHttpClient(
    applicationContext: Context,
    cacheInterceptor: Interceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor
) = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .cache(Cache(File(applicationContext.cacheDir, "network-cache"), 1L * 1024L * 1024L)) // 1 MiB
    .addNetworkInterceptor(cacheInterceptor)
    .build()

fun provideRetrofit(
    networkJson: Json,
    okHttpClient: OkHttpClient
): Retrofit = Retrofit.Builder()
    .addConverterFactory(
        networkJson.asConverterFactory(
            "application/json".toMediaType()
        )
    )
    .baseUrl(API_BASE_URL)
    .client(okHttpClient)
    .build()

private fun providePriceNetworkApi(
    retrofit: Retrofit
): RickAndMortyApi = retrofit
    .create(RickAndMortyApi::class.java)
// endregion

// region Local

// region Room
fun provideAppRoomDatabase(
    applicationContext: Context
) = AppRoomDatabase
    .createInstance(
        applicationContext = applicationContext
    )

fun provideCharacterDAODatabase(
    appRoomDatabase: AppRoomDatabase
) = appRoomDatabase
    .characterDAO()

fun provideCharacterPagingKeyDAODatabase(
    appRoomDatabase: AppRoomDatabase
) = appRoomDatabase
    .characterPagingKeyDAO()

// endregion

fun provideCharacterPager(
    localDataSource: CharacterLocalDataSource,
    networkDataSource: CharacterNetworkDataSource
) = Pager(
    config = PagingConfig(
        pageSize = 10,
        initialLoadSize = 20
    ),
    remoteMediator = CharacterRemoteMediator(
        localSource = localDataSource,
        networkSource = networkDataSource
    ),
    pagingSourceFactory = {
        localDataSource.readAllCharacters()
    }
)

