package com.rezalaki.cleancomposestore.ui.screen.commentsdialog


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.ui.components.ActionIconTypeEnum
import com.rezalaki.cleancomposestore.ui.components.ConfirmDialog
import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
import com.rezalaki.cleancomposestore.ui.components.LoadingBox
import com.rezalaki.cleancomposestore.ui.components.NewDebouncedTextFiled
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    productId: Int,
    onCloseCallBack: () -> Unit,
    commentsViewModel: CommentsViewModel = hiltViewModel<CommentsViewModel, CommentsViewModelFactory> {
        factory -> factory.create(productId)
    }
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    var confirmDeleteCommentDialog by remember {
        mutableStateOf(
            Pair(false, -1)
        )
    }

    LaunchedEffect(Unit) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            commentsViewModel.handleIntent(
                CommentsIntent.LoadComments(productId)
            )

            commentsViewModel.event.collect { e ->
                when (e) {
                    CommentsEvent.OnCloseButtonClicked -> {
                        sheetState.hide()
                        onCloseCallBack.invoke()
                    }

                    is CommentsEvent.ShowErrorMessage -> Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is CommentsEvent.ShowSuccessMessage -> Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    CommentsEvent.HideConfirmDialog -> confirmDeleteCommentDialog =
                        confirmDeleteCommentDialog.copy(first = false, second = -1)

                    is CommentsEvent.ShowConfirmDialogDeleteComment -> confirmDeleteCommentDialog =
                        confirmDeleteCommentDialog.copy(first = true, second = e.commentId)

                    else -> {}
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onCloseCallBack,
        sheetState = sheetState
    ) {
        if (confirmDeleteCommentDialog.first) {
            ConfirmDialog(
                message = "Are you sure to delete this comment?",
                yesClicked = {
                    commentsViewModel.handleIntent(
                        CommentsIntent.DeleteMyComment(confirmDeleteCommentDialog.second)
                    )
                },
                noClicked = {
                    commentsViewModel.handleIntent(
                        CommentsIntent.HideConfirmDialogDeleteMyComment
                    )
                }
            )
        }
        CommentsContent(productId, commentsViewModel)
    }

}


@Composable
private fun CommentsContent(productId: Int, commentsViewModel: CommentsViewModel) {
    val state = commentsViewModel.state.collectAsStateWithLifecycle()
    var input by rememberSaveable { mutableStateOf("") }
    val listScrollState = rememberLazyListState()
    var showLoadingOnSendIcon by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        commentsViewModel.event.collectLatest { e ->
            when (e) {
                CommentsEvent.ScrollTop -> {
                    showLoadingOnSendIcon = false
                    input = ""
                    listScrollState.animateScrollToItem(0)
                }

                is CommentsEvent.UpdateInput -> {
                    input = e.text
                }

                is CommentsEvent.ShowErrorMessage -> {
                    showLoadingOnSendIcon = false
                }

                else -> {}
            }
        }
    }

    Column {
        Text(
            text = "Comments",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        when {
            !state.value.commentsList.isNullOrEmpty() -> {
                LazyColumn(
                    modifier = Modifier.weight(1F),
                    state = listScrollState
                ) {
                    items(
                        state.value.commentsList!!,
                        key = { c -> c.id }
                    ) { comment ->
                        CommentItem(
                            comment = comment,
                            onDeleteClicked = { commentId ->
                                commentsViewModel.handleIntent(
                                    CommentsIntent.ShowConfirmDialogDeleteMyComment(commentId)
                                )
                            }
                        )
                    }
                }
                NewDebouncedTextFiled(
                    actionIconType = if (showLoadingOnSendIcon) ActionIconTypeEnum.LOADING else ActionIconTypeEnum.SEND,
                    actionIconClicked = {
                        showLoadingOnSendIcon = true
                        commentsViewModel.handleIntent(
                            CommentsIntent.AddComment(it, productId)
                        )
                    },
                    enable = !showLoadingOnSendIcon,
                    hintText = "write your comment..",
                    showSearchIcon = false,
                    debounceMillis = null,
                    value = input,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onValueChange = {
                        commentsViewModel.handleIntent(
                            CommentsIntent.UpdateInput(it)
                        )
                    },
                    onDebouncedValueChange = {}
                )
//                DebouncedTextField(
//                    inputString = input,
//                    delayMillis = 0L,
//                    onTextChange = { txt ->
//                        commentsViewModel.handleIntent(
//                            CommentsIntent.UpdateInput(txt)
//                        )
//                    },
//                    onTextChangeAfterDelay = {},
//                    hintText = "write your comment",
//                    enabled = !showLoadingOnSendIcon,
//                    asideIcon = Icons.Default.Send,
//                    onAsideIconClicked = {
//                        showLoadingOnSendIcon = true
//                        commentsViewModel.handleIntent(
//                            CommentsIntent.AddComment(input, productId)
//                        )
//                    }
//                )
            }

            state.value.isLoading -> LoadingBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            !state.value.error.isNullOrEmpty() -> ErrorBoxWithRetryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                errorMessage = state.value.error.orEmpty(),
                onBtnClicked = {
                    commentsViewModel.handleIntent(
                        CommentsIntent.LoadComments(productId)
                    )
                }
            )

            state.value.commentsList.isNullOrEmpty() -> Text(
                "No comments found for [productId=$productId]",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp),
                textAlign = TextAlign.Center,
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentItem(
    comment: Comment,
    onDeleteClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp, start = 16.dp, top = 10.dp, bottom = 8.dp)
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "",
                Modifier.size(20.dp)
            )
            Text(
                text = "${comment.user.getFullName()}:",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 0.dp)
                    .weight(1f),
                fontWeight = FontWeight.Bold
            )
            if (comment.isMine())
                IconButton(
                    modifier = Modifier.height(24.dp),
                    onClick = {
                        onDeleteClicked.invoke(comment.id)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "delete",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Gray.copy(alpha = 0.7F)
                    )
                }
        }
        Text(
            text = comment.body,
            fontSize = 13.sp,
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxWidth(),
            fontStyle = FontStyle.Italic,
        )
    }
}

