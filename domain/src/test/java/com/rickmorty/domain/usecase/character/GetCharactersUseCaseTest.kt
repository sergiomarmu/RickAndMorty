package com.rickmorty.domain.usecase.character

import com.rickmorty.domain.BaseUseCaseTest
import com.rickmorty.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest : BaseUseCaseTest() {

    /**
     * Share TestCoroutineScheduler between dispatcher
     * and runTest to ensure consistent virtual time.
     */
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @MockK
    private lateinit var mockRepository: CharacterRepository

    private lateinit var useCase: GetCharactersUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = GetCharactersUseCase(
            defaultDispatcher = testDispatcher,
            repository = mockRepository
        )
    }

    @Test
    fun should_callRepository_when_useCaseIsInvoked() = runTest(
        context = testDispatcher
    ) {
        // Arrange
        every { mockRepository.getCharactersFlow() } returns emptyFlow()

        // Act
        useCase
            .invoke()

        // Assert
        verify(exactly = 1) { mockRepository.getCharactersFlow() }
    }

}