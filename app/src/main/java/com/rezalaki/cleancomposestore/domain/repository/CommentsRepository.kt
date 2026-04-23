package com.rezalaki.cleancomposestore.domain.repository

import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.data.model.db.MyComment
import kotlinx.coroutines.flow.Flow


/**
 *  In this project we have fake comments for most of projects, which are received from
 *  our `apiService`.
 *
 *  We save user comment(s) inside our `local database`(using Room). So, when loading comments of a product,
 *  we fetch both `apiService` and `local database`.
 *
 *  NOTE: when project goes bigger, we will fully separate local data sources from remote data sources
 *
 **/
interface CommentsRepository {
    fun getComments(productId: Int): Flow<Result<List<Comment>>>

    suspend fun getMyComments(productId: Int): Flow<Result<List<MyComment>>>
    suspend fun saveMyComment(myComment: MyComment): Result<MyComment>
    suspend fun deleteMyComment(commentId: Int): Result<Int>
}