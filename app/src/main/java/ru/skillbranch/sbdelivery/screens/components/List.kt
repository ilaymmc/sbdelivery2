package ru.skillbranch.sbdelivery.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> Grid(
    items: List<T>,
    modifier: Modifier = Modifier,
    cols: Int = 2,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    cellsPadding: Dp = 8.dp,
    itemContent: @Composable BoxScope.(item: T) -> Unit
) {
    val chunkedList = items.chunked(cols)
    
    Column(
        verticalArrangement = Arrangement.spacedBy(cellsPadding),
        modifier = modifier.padding(contentPadding)
    ) {
        chunkedList.forEach { chunk ->
            Row {
                repeat(chunk.size) {
                    Box(modifier = Modifier.weight(1f / cols)) {
                        itemContent(chunk[it])
                    }
                    if (it < chunk.size.dec())
                        Spacer(modifier = Modifier.width(cellsPadding))
                }
                if (chunk.size % cols > 0) {
                    val remainCells = cols - chunk.size % cols
                    repeat(remainCells) {
                        Spacer(modifier = Modifier.width(cellsPadding))
                    }

                    Spacer(modifier = Modifier.weight(remainCells / cols.toFloat()))
                }
            }
        }
    }
}

@Composable
fun <T> LazyGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    cols: Int = 2,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    cellsPadding: Dp = 8.dp,
    itemContent: @Composable BoxScope.(item: T) -> Unit
) {
    val chunkedList = items.chunked(cols)

    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(cellsPadding),
        modifier = modifier
    ) {
        items(chunkedList) { chunk ->
            Row {
                repeat(chunk.size) {
                    Box(modifier = Modifier.weight(1f / cols)) {
                        itemContent(chunk[it])
                    }
                    if (it < chunk.size.dec())
                        Spacer(modifier = Modifier.width(cellsPadding))
                }
                if (chunk.size % cols > 0) {
                    val remainCells = cols - chunk.size % cols
                    repeat(remainCells) {
                        Spacer(modifier = Modifier.width(cellsPadding))
                    }

                    Spacer(modifier = Modifier.weight(remainCells / cols.toFloat()))
                }
            }
        }
    }
}