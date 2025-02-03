@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)

package com.rickmorty.ui.feature.characterlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rickmorty.ui.ui.theme.SchwiftyFontFamily
import com.rickmorty.ui.utils.parcelable.character.CharacterUI
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = koinViewModel<CharacterListViewModel>()
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val state = viewModel
        .state
        .collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "RickAndMorty",
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = SchwiftyFontFamily,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            letterSpacing = 4.sp,
                            fontSize = 32.sp
                        )
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            modifier = modifier.padding(innerPadding),
            navigator = navigator,
            listPane = {
                AnimatedPane {
                    if (state.loadState.refresh is LoadState.Error) LaunchedEffect(key1 = state.loadState) {
                        val result = snackBarHostState
                            .showSnackbar(
                                message = "No Internet. Displaying cached data",
                                actionLabel = "Reconnect now",
                                duration = SnackbarDuration.Indefinite
                            )

                        when (result) {
                            SnackbarResult.ActionPerformed -> state.retry()

                            SnackbarResult.Dismissed ->
                                Unit
                        }
                    }

                    Box(modifier = modifier.fillMaxSize()) {
                        when {
                            state.loadState.refresh is LoadState.Loading -> CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )

                            else -> CharacterListSuccessScreen(
                                state = state,
                                navigator = navigator
                            )
                        }
                    }
                }
            },
            detailPane = {
                (navigator.currentDestination
                    ?.content as? CharacterUI)
                    ?.let { item ->
                        AnimatedPane {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CharacterItemDetails(
                                    CharacterUI(
                                        name = item.name,
                                        status = item.status,
                                        species = item.species,
                                        gender = item.gender,
                                        originLocation = item.originLocation,
                                        lastKnownLocation = item.lastKnownLocation,
                                        image = item.image
                                    )
                                )
                            }
                        }
                    }
            }
        )
    }
}

@Composable
fun CharacterListSuccessScreen(
    state: LazyPagingItems<CharacterUI>,
    navigator: ThreePaneScaffoldNavigator<Any>,
    modifier: Modifier = Modifier
) = PullToRefreshBox(
    isRefreshing = state.loadState.refresh is LoadState.Loading,
    onRefresh = { state.refresh() },
    modifier = modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.itemCount) { index ->
            val character = state[index]

            character
                ?.let {
                    CharacterItem(
                        item = character,
                        onClick = {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                content = character
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
        }

        item {
            if (state.loadState.append is LoadState.Loading)
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
        }
    }
}

@Composable
fun CharacterItem(
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
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(item.image)
                .crossfade(true)
                .build(),
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
                color = statusColor
            )
        }
    }
}

@Composable
fun CharacterItemDetails(
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

    AsyncImage(
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(8.dp)),
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(item.image)
            .crossfade(true)
            .build(),
        contentDescription = "character image with name ${item.name}",
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

@Preview
@Composable
fun CharacterItemPreview() = CharacterItem(
    item = CharacterUI(
        name = "Rick",
        status = CharacterUI.Status.Alive,
        species = "species",
        gender = CharacterUI.Gender.Male,
        originLocation = "originLocation",
        lastKnownLocation = "lastKnownLocation",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    ),
    onClick = {},
    modifier = Modifier.fillMaxWidth()
)

@Preview
@Composable
fun CharacterItemDetailsPreview() = CharacterItemDetails(
    item = CharacterUI(
        name = "Rick",
        status = CharacterUI.Status.Alive,
        species = "species",
        gender = CharacterUI.Gender.Male,
        originLocation = "originLocation",
        lastKnownLocation = "lastKnownLocation",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    ),
    modifier = Modifier.fillMaxWidth()
)
