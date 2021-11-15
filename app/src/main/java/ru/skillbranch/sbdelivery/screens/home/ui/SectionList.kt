package ru.skillbranch.sbdelivery.screens.home.ui

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import ru.skillbranch.sbdelivery.domain.DishItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.skillbranch.sbdelivery.screens.components.Grid
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

@Composable
fun SectionList(
    dishes: List<DishItem>,
    title: String,
    modifier: Modifier = Modifier,
    limit: Int = 6,
    onClick: (DishItem) -> Unit,
    onAddToCart: (DishItem) -> Unit,
    onToggleLike: (DishItem) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (!expanded) "См.все" else "свернуть",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!expanded) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item { 
                    Spacer(modifier = Modifier.width(8.dp))
                }
                items(dishes.take(limit), {it.id}) { item ->
                    ProductItem(
                        dish = item,
                        onToggleLike = onToggleLike,
                        onAddToCart = onAddToCart,
                        onClick = onClick
                    )
                }
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        } else {
            Grid(
                items = dishes,
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp)
            ) { item ->
                ProductItem(
                    dish = item,
                    onToggleLike = onToggleLike,
                    onAddToCart = onAddToCart,
                    onClick = onClick
                )
            }
            
        }
    }
}

@Composable
fun ShimmerProductItem(
    colors: List<Color>,
    xShimmer: Float,
    yShimmer:Float,
    cardWidth: Dp,
    gradientWidth: Float,
) {
    val brush = Brush.linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer),
    )
    Card(modifier = Modifier.width(cardWidth)) {
        Column {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(brush))
            Spacer(modifier = Modifier.height(18.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Spacer(modifier = Modifier
                    .height(14.dp)
                    .width(cardWidth * .35f)
                    .background(
                        brush = brush,
                        shape = MaterialTheme.shapes.small
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier
                    .height(14.dp)
                    .width(cardWidth * .85f)
                    .background(
                        brush = brush,
                        shape = MaterialTheme.shapes.small
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier
                    .height(14.dp)
                    .width(cardWidth * .55f)
                    .background(
                        brush = brush,
                        shape = MaterialTheme.shapes.small
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ShimmerSection(
    itemWidth:Dp,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "См. все",
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val cardWidthPx = with(LocalDensity.current){ itemWidth.toPx() }
            val cardHeightPx = with(LocalDensity.current){ itemWidth.toPx() / 0.68f }
            val gradientWidth: Float = (0.4f * cardHeightPx)
            val infinityTransition = rememberInfiniteTransition()

            val xCardShimmer by infinityTransition.animateFloat(
                initialValue = 0f,
                targetValue = (cardWidthPx + gradientWidth),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1500,
                        easing = LinearEasing,
                        delayMillis = 300
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
            val yCardShimmer by infinityTransition.animateFloat(
                initialValue = 0f,
                targetValue = (cardHeightPx + gradientWidth),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1500,
                        easing = LinearEasing,
                        delayMillis = 300
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
            val colors = listOf(
                MaterialTheme.colors.onBackground.copy(alpha = .4f),
                MaterialTheme.colors.onBackground.copy(alpha = .2f),
                MaterialTheme.colors.onBackground.copy(alpha = .4f),
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                items(5) {
                    ShimmerProductItem(
                        colors = colors,
                        xShimmer = xCardShimmer,
                        yShimmer = yCardShimmer,
                        cardWidth = itemWidth,
                        gradientWidth = gradientWidth
                    )
                }
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }

    }
}

@Preview
@Composable
fun ShimmerPreview() {
    AppTheme {
        val colors = listOf(
            MaterialTheme.colors.onBackground.copy(alpha = .4f),
            MaterialTheme.colors.onBackground.copy(alpha = .2f),
            MaterialTheme.colors.onBackground.copy(alpha = .4f),
        )
        ShimmerProductItem(colors = colors, 140f, 150f, 160.dp, 100f)
    }
}