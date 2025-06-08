package com.rickmorty.ui.feature.characterlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.tools.screenshot.PreviewTest
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.utils.parcelable.character.CharacterUI
import kotlinx.coroutines.flow.flowOf
import com.rickmorty.ui.feature.characterlist.Previews as CharacterListPreviews

class CharacterListScreenshots {

    @PreviewTest
    @CharacterListPreviews
    @PreviewScreenSizes
    @Composable
    internal fun CharacterListScreenPreview(
        @PreviewParameter(CharacterListParameterProvider::class) item: List<CharacterUI>
    ) = RickMortyTheme {
        CharacterListSuccessScreen(
            state = flowOf(
                PagingData.from(
                    item,
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.NotLoading(false),
                        append = LoadState.NotLoading(false),
                        prepend = LoadState.NotLoading(false),
                    )
                ),
            ).collectAsLazyPagingItems(),
            navigateToDetail = {}
        )
    }
}

