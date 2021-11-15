package ru.skillbranch.sbdelivery.screens.menu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.domain.CategoryItem
import ru.skillbranch.sbdelivery.screens.components.LazyGrid
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

@Composable
fun MenuItem(item: CategoryItem, modifier: Modifier = Modifier, onClick: (CategoryItem) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick(item) }
    ) {

        val painter = rememberImagePainter(
            data = item.icon,
            builder = {
                crossfade(true)
                placeholder(R.drawable.img_empty_place_holder)
            })

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painter,
                contentDescription = item.title,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = item.title,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MenuItemPreview() {

    val items = listOf(
        CategoryItem("0", "test test test test", null, 0, null),
        CategoryItem("1", "test1 test1 test1 test test test test test test", null, 0, null),
        CategoryItem("2", "test2 test2 test2 test test test test test test test test test test", null, 0, null),
        CategoryItem("3", "test1 test1 test1 test test test test test test", null, 0, null),
        CategoryItem("4", "test2 test2 test2 test test test test test test test test test test",
            null, 0, null),
    )

    AppTheme {
        LazyGrid(
            items = items,
            cols = 2,
            cellsPadding = 16.dp
        ) {
            MenuItem(it) {}
        }
    }
}
