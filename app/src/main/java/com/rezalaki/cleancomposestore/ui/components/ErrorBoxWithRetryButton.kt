package com.rezalaki.cleancomposestore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ErrorBoxWithRetryButton(
    modifier: Modifier = Modifier,
    errorMessage: String,
    onBtnClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            errorMessage,
            modifier = Modifier.padding(all = 16.dp),
            textAlign = TextAlign.Center
        )
        OutlinedButton(
            modifier = Modifier,
            onClick = { onBtnClicked.invoke() }
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}


@Preview
@Composable
private fun ErrorBoxWithRetryButtonPreview() {
    ErrorBoxWithRetryButton(
        modifier = Modifier
            .width(250.dp)
            .height(150.dp)
            .background(Color.White),
        errorMessage = "something went wrong!",
        onBtnClicked = {
            // call api again
        }
    )
}