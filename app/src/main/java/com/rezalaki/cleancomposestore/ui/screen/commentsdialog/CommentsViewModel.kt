package com.rezalaki.cleancomposestore.ui.screen.commentsdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.cleancomposestore.data.model.db.MyComment
import com.rezalaki.cleancomposestore.domain.usecase.comment.DeleteMyCommentUseCase
import com.rezalaki.cleancomposestore.domain.usecase.comment.GetCommentsUseCase
import com.rezalaki.cleancomposestore.domain.usecase.comment.InsertMyCommentUseCase
import com.rezalaki.cleancomposestore.ui.screen.commentsdialog.CommentsEvent.ShowConfirmDialogDeleteComment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AssistedFactory
interface CommentsViewModelFactory {
    fun create(productId: Int): CommentsViewModel
}

@HiltViewModel(assistedFactory = CommentsViewModelFactory::class)
class CommentsViewModel @AssistedInject constructor(
    @Assisted private val productId: Int,
    private val insertMyCommentUseCase: InsertMyCommentUseCase,
    private val deleteMyCommentUseCase: DeleteMyCommentUseCase,
    private val getAllCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CommentsState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CommentsState()
        )

    private val _event = MutableSharedFlow<CommentsEvent>()
    val event = _event.asSharedFlow()

    fun handleIntent(intent: CommentsIntent) {
        viewModelScope.launch {
            when (intent) {
                is CommentsIntent.LoadComments -> loadComments()
                CommentsIntent.CloseBottomSheet -> _event.emit(CommentsEvent.OnCloseButtonClicked)
                is CommentsIntent.AddComment -> addMyComment(intent.commentText)
                is CommentsIntent.DeleteMyComment -> deleteMyComment(intent.myCommentId)

                CommentsIntent.HideConfirmDialogDeleteMyComment -> _event.emit(CommentsEvent.HideConfirmDialog)

                is CommentsIntent.ShowConfirmDialogDeleteMyComment -> _event.emit(
                    ShowConfirmDialogDeleteComment(intent.myCommentId)
                )

                is CommentsIntent.UpdateInput -> _event.emit(CommentsEvent.UpdateInput(intent.text))

            }
        }
    }

    private suspend fun addMyComment(commentText: String) {
        delay(1_000)
        val myComment = MyComment(
            body = commentText,
            productId = productId
        )
        insertMyCommentUseCase(productId, myComment)
            .onSuccess { comment ->
                val updatedComments = _state.value.commentsList.orEmpty().toMutableList()
                    .apply {
                        add(0, comment)
                    }
                _state.update {
                    it.copy(
                        commentsList = updatedComments.toList(),
                        isLoading = false
                    )
                }
                _event.emit(CommentsEvent.ScrollTop)
            }
            .onFailure {
                _state.update {
                    it.copy(isLoading = false)
                }
                _event.emit(CommentsEvent.ShowErrorMessage("error in adding new comment"))
            }
    }

    private suspend fun deleteMyComment(myCommentId: Int) {
        deleteMyCommentUseCase(myCommentId)
            .onSuccess {
                _state.update {
                    val comments = _state.value.commentsList!!.toMutableList().apply {
                        removeIf { comment -> comment.id == myCommentId }
                    }
                    it.copy(commentsList = comments)
                }
                _event.emit(CommentsEvent.ShowSuccessMessage("deleted successfully"))
                _event.emit(CommentsEvent.HideConfirmDialog)
            }
            .onFailure {
                _event.emit(CommentsEvent.ShowErrorMessage("error in deleting comment"))
            }
    }

    private suspend fun loadComments() {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(isLoading = true)
            }
            getAllCommentsUseCase.invoke(productId)
                .catch { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message
                        )
                    }
                }
                .collect { comments ->
                    _state.update {
                        it.copy(
                            commentsList = comments,
                            isLoading = false
                        )
                    }
                }
        }
    }

}