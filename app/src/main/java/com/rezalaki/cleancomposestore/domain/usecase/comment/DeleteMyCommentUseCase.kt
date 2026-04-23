package com.rezalaki.cleancomposestore.domain.usecase.comment

import com.rezalaki.cleancomposestore.domain.repository.CommentsRepository
import javax.inject.Inject


class DeleteMyCommentUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {

    suspend operator fun invoke(myCommentId: Int): Result<Int> {
        return commentsRepository.deleteMyComment(myCommentId)
    }

}