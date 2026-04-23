package com.rezalaki.cleancomposestore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun ConfirmDialog(
    message: String,
    yesText: String = "Yes",
    noText: String = "No",
    dismissOnBackPress: Boolean = true,
    yesClicked: () -> Unit,
    noClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = noClicked,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = dismissOnBackPress
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85F)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(top = 12.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(
                message,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 17.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(yesClicked) {
                    Text(yesText)
                }
                TextButton(noClicked) {
                    Text(noText)
                }
            }
        }
    }
}


@Preview
@Composable
private fun ConfirmDialogPreview() {
    ConfirmDialog(
        "Are you sure to delete?",
        yesClicked = {},
        noClicked = {}
    )
}

