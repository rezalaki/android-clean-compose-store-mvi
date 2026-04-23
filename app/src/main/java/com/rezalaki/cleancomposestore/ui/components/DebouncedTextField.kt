package com.rezalaki.cleancomposestore.ui.components


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezalaki.cleancomposestore.ui.theme.inputBorderColorFocused
import com.rezalaki.cleancomposestore.ui.theme.inputBorderColorUnfocused
import com.rezalaki.cleancomposestore.ui.theme.inputContainerColorFocused
import com.rezalaki.cleancomposestore.ui.theme.inputContainerColorUnfocused
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


enum class ActionIconTypeEnum {
    CLEAR,
    LOADING,
    SEND
}

@OptIn(FlowPreview::class)
@Composable
fun NewDebouncedTextFiled(
    actionIconType: ActionIconTypeEnum,
    actionIconClicked: (String) -> Unit,
//    actionButton: @Composable () -> Unit,
    enable: Boolean,
    hintText: String? = null,
    showSearchIcon: Boolean,
    debounceMillis: Long? = null,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onDebouncedValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            shape = RoundedCornerShape(16.dp),
            enabled = enable,
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (hintText != null)
                    Text(hintText, color = Color.Black.copy(alpha = 0.35F))
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            singleLine = true,
            leadingIcon = if (!isFocused && showSearchIcon) {
                {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            } else {
                null
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = value.isNotEmpty(),
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    when (actionIconType) {
                        ActionIconTypeEnum.CLEAR -> IconButton(onClick = {
                            actionIconClicked.invoke("")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear text"
                            )
                        }

                        ActionIconTypeEnum.LOADING -> CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center),
                            strokeWidth = 3.dp
                        )

                        ActionIconTypeEnum.SEND -> IconButton(onClick = {
                            actionIconClicked.invoke(value)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Clear text"
                            )
                        }
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = inputContainerColorUnfocused,
                unfocusedBorderColor = inputBorderColorUnfocused,

                focusedContainerColor = inputContainerColorFocused,
                focusedBorderColor = inputBorderColorFocused,

                focusedTextColor = Color.Black.copy(alpha = 0.85F),
                unfocusedTextColor = Color.Black.copy(alpha = 0.85F),
                cursorColor = Color.Black.copy(alpha = 0.85F),

                disabledContainerColor = Color.LightGray.copy(alpha = 0.5F)
            )
        )
    }

    if (debounceMillis != null) {
        val debouncedFlow = remember(value, debounceMillis) {
            snapshotFlow { value.trim() }
                .debounce(debounceMillis)
                .distinctUntilChanged()
        }

        LaunchedEffect(debouncedFlow) {
            debouncedFlow.collect { trimmed ->
                onDebouncedValueChange(trimmed)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewDebouncedTextFiledPreview() {
    NewDebouncedTextFiled(
        showSearchIcon = true,
        actionIconType = ActionIconTypeEnum.LOADING,
        actionIconClicked = {

        },
        enable = true,
        value = "",
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {},
        onDebouncedValueChange = {},
        hintText = "",
//        actionButton = {
//            Text(
//                text = "asdasd",
//                modifier = Modifier
//                    .width(50.dp)
//                    .clickable {
//                        Log.d("TAGGGG", "NewDebouncedTextFiledPreview: ")
//                    }
//            )
//        }
    )
}


@Composable
fun DebouncedTextField(
    inputString: String,
    delayMillis: Long,
    hintText: String,
    minLine: Int = 1,
    maxLine: Int = 2,
    asideIcon: ImageVector,
    enabled: Boolean,
    onAsideIconClicked: () -> Unit,
    onTextChange: (String) -> Unit,
    onTextChangeAfterDelay: (String?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray.copy(alpha = 0.1F))
            .border(
                width = 1.dp,
                color = Color.DarkGray.copy(alpha = 0.15F),
                shape = RoundedCornerShape(12.dp)
            ),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(end = 32.dp),
            value = inputString,
            onValueChange = { txt: String ->
                onTextChange.invoke(txt)
            },
            textStyle = TextStyle.Default.copy(
                fontSize = 16.sp,
            ),
            enabled = enabled,
            maxLines = maxLine,
            minLines = minLine
        )
        if (!enabled) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp, top = 6.dp),
                strokeWidth = 3.dp,
            )
        } else if (inputString.isEmpty()) {
            Text(
                hintText,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                textAlign = TextAlign.Start,
                color = Color.DarkGray.copy(alpha = 0.45F)
            )
        } else {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    onAsideIconClicked.invoke()
                },
            ) {
                Icon(
                    imageVector = asideIcon,
                    contentDescription = "submit",
                    tint = Color.DarkGray.copy(alpha = 0.6F)
                )
            }
        }

    }

    if (delayMillis != 0L) {
        LaunchedEffect(inputString) {
            snapshotFlow { inputString }
                .map { it.trim() }
                .filter { it.length > 2 }
                .debounce(delayMillis)
                .distinctUntilChanged()
                .collectLatest {
                    onTextChangeAfterDelay.invoke(it)
                }
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//private fun DebouncedTextFieldPreview(){
//    DebouncedTextField(
//        inputString = "asus n580vd",
//        delayMillis = 200L,
//        hintText = "product name...",
//        asideIcon = Icons.Default.Send,
//        enabled = true,
//        onAsideIconClicked = {},
//        onTextChange = {},
//        onTextChangeAfterDelay = {}
//    )
//}

