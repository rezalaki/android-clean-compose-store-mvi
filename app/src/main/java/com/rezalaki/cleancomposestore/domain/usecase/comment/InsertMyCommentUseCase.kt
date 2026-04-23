package com.rezalaki.cleancomposestore.domain.usecase.comment

import com.rezalaki.cleancomposestore.data.model.db.MyComment
import com.rezalaki.cleancomposestore.data.model.db.toComment
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.domain.repository.CommentsRepository
import javax.inject.Inject

class InsertMyCommentUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {

    suspend operator fun invoke(productId: Int, myComment: MyComment): Result<Comment> {
        val result = commentsRepository.saveMyComment(myComment)
        if (result.isSuccess) {
            val myNewComment = result.getOrThrow().toComment()
            return Result.success(myNewComment)
        } else {
            return Result.failure(result.exceptionOrNull() ?: Throwable("Error in inserting"))
        }
    }

}