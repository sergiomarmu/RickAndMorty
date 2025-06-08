package com.rickmorty.ui.feature.characterlist.item

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rickmorty.ui.R
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.utils.parcelable.character.CharacterUI

@Composable
fun CharacterListItem(
    item: CharacterUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = ElevatedCard(
    onClick = onClick,
    modifier = modifier
        .height(120.dp)
        .padding(horizontal = 16.dp)
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(
                size = 12.dp
            )
        ),
    colors = CardDefaults.elevatedCardColors()
        .copy(containerColor = MaterialTheme.colorScheme.onPrimary),
    elevation = CardDefaults
        .cardElevation(defaultElevation = 4.dp)
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val model = ImageRequest
            .Builder(LocalContext.current)
            .data(
                if (LocalInspectionMode.current)
                    R.drawable.crocubot
                else
                    item.image
            )
            .crossfade(true)
            .build()

        AsyncImage(
            model = model,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = "character image with name ${item.name}",
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            val statusColor = when (item.status) {
                CharacterUI.Status.Alive -> MaterialTheme.colorScheme.primary
                CharacterUI.Status.Dead -> Color.Red
                CharacterUI.Status.Unknown -> Color.Gray
            }

            Text(
                text = item.status.toString(),
                color = Color.Red
            )
        }
    }
}

@Preview(name = "CharacterListItem -- Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "CharacterListItem -- Dark Mode", uiMode = UI_MODE_NIGHT_YES)
annotation class Previews

@Previews
@Composable
internal fun CharacterListItemPreview(
    @PreviewParameter(CharacterListItemParameterProvider::class) item: CharacterUI
) = RickMortyTheme {
    CharacterListItem(
        item = item,
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
    )
}