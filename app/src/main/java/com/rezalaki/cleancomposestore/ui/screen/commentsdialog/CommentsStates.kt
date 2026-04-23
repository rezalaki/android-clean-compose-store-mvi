package com.rezalaki.cleancomposestore.ui.screen.commentsdialog

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.response.Comment


@Stable
data class CommentsState(
    val isLoading: Boolean = false,
    val commentsList: List<Comment>? = null,
    val error: String? = null
)

sealed class CommentsIntent {
    data class LoadComments(val productId: Int) : CommentsIntent()
    data class AddComment(val commentText: String, val productId: Int) : CommentsIntent()

    data class ShowConfirmDialogDeleteMyComment(val myCommentId: Int): CommentsIntent()
    object HideConfirmDialogDeleteMyComment: CommentsIntent()
    data class DeleteMyComment(val myCommentId: Int) : CommentsIntent()

    data class UpdateInput(val text: String) : CommentsIntent()

    object CloseBottomSheet : CommentsIntent()
}


sealed interface CommentsEvent {
    data class ShowConfirmDialogDeleteComment(val commentId: Int) : CommentsEvent
    object HideConfirmDialog : CommentsEvent

    object OnCloseButtonClicked : CommentsEvent
    object ScrollTop : CommentsEvent

    data class ShowErrorMessage(val message: String) : CommentsEvent
    data class ShowSuccessMessage(val message: String) : CommentsEvent

    data class UpdateInput(val text: String) : CommentsEvent
}