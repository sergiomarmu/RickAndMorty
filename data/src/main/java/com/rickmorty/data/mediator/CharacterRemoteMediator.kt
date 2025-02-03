package com.rickmorty.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rickmorty.data.datasource.local.character.CharacterLocalDataSource
import com.rickmorty.data.datasource.local.character.dao.KEY_ID
import com.rickmorty.data.datasource.local.character.entity.CharacterEntity
import com.rickmorty.data.datasource.local.character.entity.CharacterRemoteKey
import com.rickmorty.data.datasource.network.character.CharacterNetworkDataSource
import com.rickmorty.data.handler.DataException
import com.rickmorty.data.mapper.character.toEntity

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val localSource: CharacterLocalDataSource,
    private val networkSource: CharacterNetworkDataSource
) : RemoteMediator<Int, CharacterEntity>() {

    // Begin a database transaction to ensure atomicity
    private suspend fun <T> withAtomicity(
        block: suspend () -> T
    ) = localSource.db.withTransaction {
        block()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            /**
             * Determine the page number to load based on the load type
             *
             * For [LoadType.REFRESH], load the first page (page 1)
             * For [LoadType.PREPEND], if there are no more previous pages, return success with end of pagination reached
             * For [LoadType.APPEND], check if there is a next page in the remote key; if not, return success with end of pagination reached
             */
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val remoteKey = withAtomicity {
                        localSource.getKeyById(KEY_ID)
                    } ?: return MediatorResult.Success(true)

                    if (remoteKey.nextPage == null)
                        return MediatorResult.Success(true)

                    remoteKey.nextPage
                }
            }

            // Fetch characters from api
            val response = networkSource.getCharactersWithPaging(page)

            val characters = response.results

            // Begin a database transaction to ensure atomicity
            withAtomicity {
                // If it's a refresh, delete all previous data
                if (loadType == LoadType.REFRESH) {
                    localSource.deleteAllCharacters()
                    localSource.deleteAllKeys()
                }

                // Determine the next page number to load, or null if there are no more characters
                val nextPage = if (characters.isNotEmpty())
                    page + 1
                else
                    null

                val characterEntities = characters.map { it.toEntity() }

                // Insert the pagination key in the local database
                localSource
                    .insertKey(
                        CharacterRemoteKey(
                            id = KEY_ID,
                            nextPage = nextPage
                        )
                    )

                // Insert/update the character entities in the local database
                localSource.upsertAllCharacters(characterEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: DataException) {
            MediatorResult.Error(e)
        }
    }
}