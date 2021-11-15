package ru.skillbranch.sbdelivery.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.screens.root.ui.AppTheme

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(MaterialTheme.colors.surface),

        ) {
            Image(
//                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.wallpaper),
                contentScale = ContentScale.Crop,
                contentDescription = "Wallpaper",
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "О приложении SBDelivery",
                        modifier = Modifier.padding(start = 48.dp),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painterResource(id = R.drawable.ic_baseline_close_24),
                            contentDescription = "Close",
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        "Version 1.0",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = onDismiss,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Ok",
                            style = MaterialTheme.typography.button,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AboutPreview() {
    AppTheme {
        AboutDialog {}
    }
}

