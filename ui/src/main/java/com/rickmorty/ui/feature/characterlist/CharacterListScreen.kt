@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)

package com.rickmorty.ui.feature.characterlist

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rickmorty.ui.feature.characterlist.detail.CharacterDetail
import com.rickmorty.ui.feature.characterlist.item.CharacterListItem
import com.rickmorty.ui.ui.theme.RickMortyTheme
import com.rickmorty.ui.ui.theme.SchwiftyFontFamily
import com.rickmorty.ui.utils.parcelable.character.CharacterUI
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
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
                                navigateToDetail = {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail,
                                        content = it
                                    )
                                }
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
                                CharacterDetail(
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
    navigateToDetail: (CharacterUI) -> Unit,
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
                    CharacterListItem(
                        item = character,
                        onClick = { navigateToDetail(character) },
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

@Preview(name = "CharacterListSuccessScreen -- Light Mode", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "CharacterListSuccessScreen -- Dark Mode", uiMode = UI_MODE_NIGHT_YES)
annotation class Previews

@Previews
@Composable
fun CharacterListSuccessScreenPreview(
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
