@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)

package com.rickmorty.data.datasource.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rickmorty.data.datasource.DataSourceBaseTest
import com.rickmorty.data.datasource.local.character.CharacterLocalDataSource
import com.rickmorty.data.datasource.network.character.CharacterNetworkDataSource
import com.rickmorty.data.datasource.network.character.dto.CharacterDTO
import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingInfoDTO
import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingWrapperDTO
import com.rickmorty.data.handler.DataException
import com.rickmorty.data.mediator.CharacterRemoteMediator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CharacterRemoteMediatorTest : DataSourceBaseTest() {

    @RelaxedMockK
    private lateinit var localSource: CharacterLocalDataSource

    @MockK
    private lateinit var networkSource: CharacterNetworkDataSource

    private lateinit var mediator: CharacterRemoteMediator

    @Before
    override fun setUp() {
        super.setUp()

        mediator = CharacterRemoteMediator(localSource, networkSource)
    }

    @Test
    fun should_returnSuccessResult_when_dataIsLoadedFromRemote_andStoredLocally_onFirstRefresh() =
        runTest {
            // Arrange
            val info = CharacterPagingInfoDTO(
                count = 100,
                pages = 30,
                next = "https://rickandmortyapi.com/api/character/?page=1",
                prev = null
            )

            val characters = listOf(
                CharacterDTO(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    origin = CharacterDTO.Origin("Earth (C-137)"),
                    location = CharacterDTO.Location("Citadel of Ricks"),
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                )
            )

            val fakeResponse = CharacterPagingWrapperDTO(
                info = info,
                results = characters
            )

            // Mocks the static `withTransaction` function of RoomDatabase, captures the suspend block passed to the transaction,
            // and manually invokes it during the test to avoid interacting with an actual database.
            mockkStatic("androidx.room.RoomDatabaseKt")

            val transaction = slot<suspend () -> Any>()
            coEvery { localSource.db.withTransaction(capture(transaction)) } coAnswers {
                transaction.captured()
            }

            coEvery { networkSource.getCharactersWithPaging(1) } returns fakeResponse

            // Act
            val result = mediator
                .load(
                    loadType = LoadType.REFRESH,
                    PagingState(
                        pages = emptyList(),
                        null,
                        config = PagingConfig(10),
                        leadingPlaceholderCount = 0
                    )
                )

            // Assert
            coVerify(exactly = 1) { networkSource.getCharactersWithPaging(1) }

            coVerify(exactly = 1) { localSource.deleteAllCharacters() }
            coVerify(exactly = 1) { localSource.deleteAllKeys() }

            coVerify(exactly = 1) { localSource.insertKey(any()) }
            coVerify(exactly = 1) { localSource.upsertAllCharacters(any()) }

            assert(result is RemoteMediator.MediatorResult.Success)
        }

    @Test
    fun should_returnFailureResult_when_networkSourceReturnsError() =
        runTest {
            // Arrange

            // Mocks the static `withTransaction` function of RoomDatabase, captures the suspend block passed to the transaction,
            // and manually invokes it during the test to avoid interacting with an actual database.
            mockkStatic("androidx.room.RoomDatabaseKt")

            val transaction = slot<suspend () -> Any>()
            coEvery { localSource.db.withTransaction(capture(transaction)) } coAnswers {
                transaction.captured()
            }

            coEvery { networkSource.getCharactersWithPaging(1) } throws DataException.Network.Unexpected()

            // Act
            val result = mediator
                .load(
                    loadType = LoadType.REFRESH,
                    PagingState(
                        pages = emptyList(),
                        null,
                        config = PagingConfig(10),
                        leadingPlaceholderCount = 0
                    )
                )

            // Assert
            coVerify(exactly = 1) { networkSource.getCharactersWithPaging(1) }
            coVerify(exactly = 0) { localSource.insertKey(any()) }
            coVerify(exactly = 0) { localSource.upsertAllCharacters(any()) }

            assert(result is RemoteMediator.MediatorResult.Error)
            assert((result as RemoteMediator.MediatorResult.Error).throwable is DataException.Network.Unexpected)
        }
}