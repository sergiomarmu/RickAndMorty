package com.rickmorty.ui.feature.characterlist

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.rickmorty.domain.model.CharacterModel
import com.rickmorty.domain.usecase.character.GetCharactersUseCase
import com.rickmorty.ui.BaseViewModelTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CharacterListViewModelTest : BaseViewModelTest() {

    @MockK
    private lateinit var mockUseCase: GetCharactersUseCase

    private lateinit var viewModel: CharacterListViewModel

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun should_callUseCase_when_stateIsSubscribed() = runTest {
        // Arrange
        val itemsExpectedResult = emptyList<CharacterModel>()

        val pagingDataFlow = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3
            ),
            initialKey = null,
            pagingSourceFactory = {
                itemsExpectedResult.asPagingSourceFactory()
                    .invoke()
            }
        ).flow

        every { mockUseCase() } returns pagingDataFlow

        // Act
        viewModel = CharacterListViewModel(mockUseCase)

        viewModel
            .state
            .asSnapshot()

        // Assert
        verify(exactly = 1) { mockUseCase() }
    }

    @Test
    fun should_returnAPagingDataWithItems_when_stateIsSubscribed() = runTest {
        // Arrange
        val itemsExpectedResult = listOf(
            CharacterModel(
                name = "rick",
                status = CharacterModel.Status.Alive,
                species = "species",
                gender = CharacterModel.Gender.Male,
                originLocation = "originLocation",
                lastKnownLocation = "lastKnownLocation",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            )
        )

        val pagingDataFlow = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3
            ),
            initialKey = null,
            pagingSourceFactory = {
                itemsExpectedResult.asPagingSourceFactory()
                    .invoke()
            }
        ).flow

        every { mockUseCase() } returns pagingDataFlow

        // Act
        viewModel = CharacterListViewModel(mockUseCase)

        val itemsResult = viewModel
            .state
            .asSnapshot()

        // Assert
        assert(itemsResult.size == itemsExpectedResult.size)
        assert(itemsResult[0].name == itemsExpectedResult[0].name)
    }
}