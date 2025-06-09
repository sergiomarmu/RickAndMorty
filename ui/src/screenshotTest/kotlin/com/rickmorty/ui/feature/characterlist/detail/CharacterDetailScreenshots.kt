package com.rickmorty.ui.feature.characterlist.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.android.tools.screenshot.PreviewTest
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.utils.parcelable.character.CharacterUI
import com.rickmorty.ui.feature.characterlist.detail.Previews as CharacterDetailPreviews

class CharacterDetailScreenshots {

    @PreviewTest
    @CharacterDetailPreviews
    @PreviewScreenSizes
    @Composable
    fun CharacterListItemPreview(
        @PreviewParameter(CharacterDetailParameterProvider::class) item: CharacterUI
    ) = RickMortyTheme {
        CharacterDetail(
            item = item,
            modifier = Modifier.fillMaxWidth()
        )
    }

}
