package ru.skillbranch.sbdelivery.screens.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.screens.dishes.data.DishesUiState
import ru.skillbranch.sbdelivery.screens.home.logic.HomeFeature
import ru.skillbranch.sbdelivery.screens.root.logic.Msg

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(state: HomeFeature.State, accept: (Msg) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Card(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentScale = ContentScale.Crop,
                contentDescription = "Wallpaper",
            )
        }

//        ShimmerSection(itemWidth = 160.dp, title = "ТЕст")

        when(state.recommended) {
            is DishesUiState.Empty -> {
                /*TODO*/
            }
            is DishesUiState.Error -> {
                /*TODO*/
            }
            is DishesUiState.Loading -> ShimmerSection(itemWidth = 160.dp, title = "Рекомендуем")
            is DishesUiState.Value -> SectionList(
                dishes = state.recommended.dishes,
                title = "Рекомендуем",
                onClick = { accept(Msg.ClickDish(it.id, it.title)) },
                onAddToCart = { accept(Msg.AddToCart(it.id, it.title)) },
                onToggleLike = { accept(Msg.ToggleLike(it.id, !it.isFavorite)) },
            )
        }
        when(state.best) {
            is DishesUiState.Empty -> {
                /*TODO*/
            }
            is DishesUiState.Error -> {
                /*TODO*/
            }
            is DishesUiState.Loading -> ShimmerSection(itemWidth = 160.dp, title = "Лучшие")
            is DishesUiState.Value -> SectionList(
                dishes = state.best.dishes,
                title = "Лучшие",
                onClick = { accept(Msg.ClickDish(it.id, it.title)) },
                onAddToCart = { accept(Msg.AddToCart(it.id, it.title)) },
                onToggleLike = { accept(Msg.ToggleLike(it.id, !it.isFavorite)) },
            )
        }
        when(state.popular) {
            is DishesUiState.Empty -> {
                /*TODO*/
            }
            is DishesUiState.Error -> {
                /*TODO*/
            }
            is DishesUiState.Loading -> ShimmerSection(itemWidth = 160.dp, title = "Популярные")
            is DishesUiState.Value -> SectionList(
                dishes = state.popular.dishes,
                title = "Популярные",
                onClick = { accept(Msg.ClickDish(it.id, it.title)) },
                onAddToCart = { accept(Msg.AddToCart(it.id, it.title)) },
                onToggleLike = { accept(Msg.ToggleLike(it.id, !it.isFavorite)) },
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Preview
@Composable
fun Preview() {
    HomeScreen(HomeFeature.initialState(), {})
}



