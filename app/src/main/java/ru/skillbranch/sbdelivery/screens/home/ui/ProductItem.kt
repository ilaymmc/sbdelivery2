package ru.skillbranch.sbdelivery.screens.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.Scale
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.domain.DishItem
import ru.skillbranch.sbdelivery.screens.dish.logic.DishFeature
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

@Composable
fun ProductItem(
    dish: DishItem,
    modifier: Modifier = Modifier,
    onToggleLike: (DishItem) -> Unit,
    onAddToCart: (DishItem) -> Unit,
    onClick: (DishItem) -> Unit,
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .defaultMinSize(minWidth = 160.dp)
        .clickable { onClick(dish) }
    ) {
        ConstraintLayout() {
            val (fab, title, poster, price, sale, favorite) = createRefs()

            val painter = rememberImagePainter(
                data = dish.image,
                builder = {
                    crossfade(true)
                    scale(Scale.FILL)
                    placeholder(R.drawable.img_empty_place_holder)
                    error(R.drawable.img_empty_place_holder)
                }
            )

            Image(
                painter = painter,
                contentDescription = dish.title,
                contentScale = if (painter.state is ImagePainter.State.Success)
                    ContentScale.Crop else ContentScale.Inside,
                modifier = Modifier.constrainAs(poster) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.aspectRatio(1f)
            )
            if (dish.isSale) {
                Text(
                    text = "АКЦИЯ",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier
                        .width(56.dp)
                        .background(
                            color = MaterialTheme.colors.secondaryVariant,
                            shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                        )
                        .constrainAs(sale) {
                            top.linkTo(parent.top, margin = 16.dp)
                            start.linkTo(parent.start)
                        }
                )
            }

            IconButton(
                onClick = { onToggleLike(dish) },
                modifier = Modifier.constrainAs(favorite) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                } 
            ) {
                Icon(
                    tint = if (dish.isFavorite) MaterialTheme.colors.secondaryVariant else
                        MaterialTheme.colors.onPrimary,
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Toggle favorite"
                )
            }
            
            Text(
                text = "${dish.price} руб.",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.constrainAs(price) {
                    width = Dimension.fillToConstraints
                    top.linkTo(poster.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                }
            )

            Text(
                text = dish.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .defaultMinSize(minHeight = 36.dp)
                    .constrainAs(title) {
                        width = Dimension.fillToConstraints
                        top.linkTo(price.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
            )

            FloatingActionButton(
                onClick = { onAddToCart(dish) },
                modifier = Modifier
                    .requiredSize(40.dp)
                    .constrainAs(fab) {
                        top.linkTo(poster.bottom)
                        bottom.linkTo(poster.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_product),
                    contentDescription = "Add to cart",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun ProductItemPreview() {
    AppTheme {
        val dish = DishItem(
            id = "212",
            title = "Бургер \"Америка\"",
            image = "https://kubnews" +
                ".ru/upload/resize_cache/iblock/cd6/1200_630_2/cd6e8588b1dffd7ce6aff2d2a6711e17" +
                ".jpg",
            price = "234",
            isFavorite = true,
            isSale = true
        )
        ProductItem(dish = dish, onToggleLike = {}, onAddToCart = {}, onClick = {},
            modifier = Modifier.width(160.dp)
        )
    }
}
