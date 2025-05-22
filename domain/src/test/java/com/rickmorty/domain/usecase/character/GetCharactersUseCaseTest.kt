package com.rickmorty.domain.usecase.character

import com.rickmorty.domain.BaseUseCaseTest
import com.rickmorty.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest : BaseUseCaseTest() {

    @MockK
    private lateinit var mockRepository: CharacterRepository

    private lateinit var useCase: GetCharactersUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetCharactersUseCase(mockRepository)
    }

    @Test
    fun should_callRepository_when_useCaseIsInvoked() = runTest {
        // Arrange
        every { mockRepository.getCharactersFlow() } returns emptyFlow()

        // Act
        useCase
            .invoke()

        // Assert
        verify(exactly = 0) { mockRepository.getCharactersFlow() }
    }

}