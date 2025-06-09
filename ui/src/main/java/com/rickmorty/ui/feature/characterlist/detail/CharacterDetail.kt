package com.rickmorty.ui.feature.characterlist.detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.utils.parcelable.character.CharacterUI

@Composable
fun CharacterDetail(
    item: CharacterUI,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .padding(8.dp)
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Spacer(modifier = Modifier.height(16.dp))

    val model = ImageRequest
        .Builder(LocalContext.current)
        .data(
            item.image
        )
        .crossfade(true)
        .build()

    AsyncImage(
        model = model,
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentDescription = "character image with name ${item.name}"
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = item.name,
        fontSize = 30.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = "STATUS",
                    modifier = Modifier
                        .weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                val statusColor = when (item.status) {
                    CharacterUI.Status.Alive -> MaterialTheme.colorScheme.primary
                    CharacterUI.Status.Dead -> Color.Red
                    CharacterUI.Status.Unknown -> Color.Gray
                }

                Text(
                    text = item.status.toString(),
                    modifier = Modifier.weight(1f),
                    color = statusColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "SPECIES",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = item.species,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "GENDER",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = item.gender.toString(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationTitleText(text = "ORIGIN LOCATION")

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = item.originLocation)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationTitleText(text = "LAST KNOWN LOCATION")

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = item.lastKnownLocation)
            }
        }
    }
}

@Composable
fun LocationTitleText(
    text: String,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    modifier = modifier
        .border(
            width = 1.dp,
            color = Color.Gray,
            RoundedCornerShape(2.dp)
        )
        .padding(horizontal = 8.dp, vertical = 4.dp)
)

@Preview(name = "Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
annotation class Previews

@Previews
@Composable
internal fun CharacterDetailPreview(
    @PreviewParameter(CharacterDetailParameterProvider::class) item: CharacterUI
) = RickMortyTheme {
    CharacterDetail(
        item = item,
        modifier = Modifier.fillMaxWidth()
    )
}