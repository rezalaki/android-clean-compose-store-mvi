package com.rezalaki.cleancomposestore.domain.usecase.comment

import com.rezalaki.cleancomposestore.data.model.db.toComment
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.domain.repository.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


class GetCommentsUseCase @Inject constructor(
    private val commentsRepository: CommentsRepository
) {

    suspend operator fun invoke(productId: Int): Flow<List<Comment>> {
        return combine(
            commentsRepository.getComments(productId),
            commentsRepository.getMyComments(productId)

        ) { remoteComments, myComments ->

            /**
             * In the returning list, my comments(which are stored locally) are first and then remote ones
             *
             * */
            myComments.getOrThrow().map { it.toComment() } + remoteComments.getOrThrow()
        }
    }

}