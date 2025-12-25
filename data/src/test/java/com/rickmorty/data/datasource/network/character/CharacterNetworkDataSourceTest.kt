@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rickmorty.data.datasource.network.character

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rickmorty.data.datasource.DataSourceBaseTest
import com.rickmorty.data.datasource.network.character.api.RickAndMortyApi
import com.rickmorty.data.datasource.network.character.dto.CharacterDTO
import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingInfoDTO
import com.rickmorty.data.datasource.network.character.dto.paging.CharacterPagingWrapperDTO
import com.rickmorty.data.handler.DataException
import com.rickmorty.data.utils.JsonResourceReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class CharacterNetworkDataSourceTest : DataSourceBaseTest() {

    companion object {
        private const val PORT_SERVER = 8080
        private const val BASE_URL = "/"
        private const val CHARACTERS_JSON_PATH_CORRECT_RESPONSE =
            "network_characters_correct_response.json"
        private const val CHARACTERS_JSON_PATH_BAD_RESPONSE = "network_characters_bad_response.json"
    }

    /**
     * Share TestCoroutineScheduler between dispatcher
     * and runTest to ensure consistent virtual time.
     */
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private lateinit var fakeServer: MockWebServer
    private lateinit var api: RickAndMortyApi
    private lateinit var dataSource: CharacterNetworkDataSource

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
    private val jsonResourceReader = JsonResourceReader()

    @Before
    override fun setUp() {
        // Init server
        fakeServer = MockWebServer()
        fakeServer.start(PORT_SERVER)

        // Create API
        val json = Json { ignoreUnknownKeys = true }

        api = Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .baseUrl(fakeServer.url(BASE_URL))
            .client(okHttpClient)
            .build()
            .create(RickAndMortyApi::class.java)

        // Create Source
        dataSource = CharacterNetworkDataSourceImpl(
            ioDispatcher = testDispatcher,
            api = api
        )
    }

    @Test
    fun should_returnASuccessResult_when_apiIsCalled() = runTest(
        context = testDispatcher
    ) {
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

        val expectedResult = CharacterPagingWrapperDTO(
            info = info,
            results = characters
        )

        val jsonResponse = jsonResourceReader.fromPath(CHARACTERS_JSON_PATH_CORRECT_RESPONSE)

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(jsonResponse)
        }

        // Act
        fakeServer.enqueue(mockResponse)

        val result = dataSource
            .getCharactersWithPaging(1)

        // Assert
        assert(result == expectedResult)
    }

    @Test(expected = DataException.Network.Unexpected::class)
    fun should_returnAUnexpectedException_when_apiIsCalledAndServerIsDown() = runTest(
        context = testDispatcher
    ) {
        // Arrange
        val mockResponse = MockResponse().apply {
            setResponseCode(500)
        }

        // Act
        fakeServer.enqueue(mockResponse)

        dataSource
            .getCharactersWithPaging(1)

        // Assert
        // Throw DataException.Network.Unexpected exception
    }

    @Test(expected = DataException.Network.Unparseable::class)
    fun should_returnAUnparseableException_when_apiIsCalledAndResponseCannotBeParsed() = runTest(
        context = testDispatcher
    ) {
        // Arrange
        val jsonResponse = jsonResourceReader.fromPath(CHARACTERS_JSON_PATH_BAD_RESPONSE)

        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(jsonResponse)
        }

        // Act
        fakeServer.enqueue(mockResponse)

        dataSource
            .getCharactersWithPaging(1)

        // Assert
        // Throw DataException.Network.Unparseable exception
    }

    @After
    fun closeServer() {
        fakeServer.shutdown()
    }
}