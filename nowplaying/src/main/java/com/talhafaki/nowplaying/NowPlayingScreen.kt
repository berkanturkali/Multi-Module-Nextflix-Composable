package com.talhafaki.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.talhafaki.common.items.MovieItem
import com.talhafaki.common.loading.ShimmerAnimation
import com.talhafaki.common.theme.NextflixComposableTheme
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import com.talhafaki.domain.entity.NetworkMovie

/**
 * Created by tfakioglu on 12.December.2021
 */
@Composable
fun NowPlayingScreen() {
    val viewModel = hiltViewModel<NowPlayingViewModel>()

    NextflixComposableTheme(darkTheme = true) {
        NowPlayingList(movieList = viewModel.nowPlayingList().collectAsLazyPagingItems())
    }
}

@Composable
fun NowPlayingList(movieList: LazyPagingItems<NetworkMovie>) {
    LazyColumn(modifier = Modifier.background(color = Color.DarkGray)) {
        items(movieList.itemCount) { index ->
            movieList[index]?.let { movie ->
                MovieItem(
                    posterPath = movie.posterUrl,
                    title = movie.title,
                    desc = movie.overview
                )
            }
        }

        movieList.apply {
            val error = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }

            val loading = when {
                loadState.prepend is LoadState.Loading -> loadState.prepend as LoadState.Loading
                loadState.append is LoadState.Loading -> loadState.append as LoadState.Loading
                loadState.refresh is LoadState.Loading -> loadState.refresh as LoadState.Loading
                else -> null
            }

            if (loading != null) {
                repeat((0..20).count()) {
                    item {
                        Box(
                            modifier = Modifier
                                .background(color = Color.DarkGray)
                        ) {
                            ShimmerAnimation()
                        }
                    }
                }
            }

            if (error != null) {
                //TODO: add error handler
                item { SweetError(message = error.error.localizedMessage ?: "Error") }
            }
        }
    }
}
