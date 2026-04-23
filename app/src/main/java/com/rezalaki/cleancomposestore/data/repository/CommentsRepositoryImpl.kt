package com.rezalaki.cleancomposestore.data.repository

import com.rezalaki.cleancomposestore.data.local.MyCommentsDao
import com.rezalaki.cleancomposestore.data.model.db.MyComment
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.data.remote.ApiService
import com.rezalaki.cleancomposestore.domain.repository.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CommentsRepositoryImpl @Inject constructor(
    private val apiService: ApiService, private val myCommentsDao: MyCommentsDao
) : CommentsRepository {


    /**
     *  Because of using json files as mocked data for remote data source[apiService], we fetch all comments and then filter by `productId`
     *  (which comes from argument) and return the found comment. That's why this method use only [apiService], NOT [myCommentsDao].
     *
     *  User own comments are stored into local database.
     *
     * */
    override fun getComments(productId: Int): Flow<Result<List<Comment>>> =
        flow {
            val comments = apiService.getComments(productId).execute()
            if (comments.isSuccessful && comments.body() != null) {
                val productComments = comments.body()!!.filter { it.productId == productId }
                emit(
                    Result.success(productComments)
                )
            } else {
                emit(
                    Result.failure(Exception("failed to fetch data from remote data source"))
                )
            }
        }


    /**
     * Since we store user own comments into local database, this method is only used to fetch user
     * comments using [myCommentsDao] and NOT [apiService].
     *
     * */
    override suspend fun getMyComments(productId: Int): Flow<Result<List<MyComment>>> =
        flow {
            val myComments = myCommentsDao.getMyComments(productId)
            emit(
                Result.success(myComments)
            )
        }.catch { throwable ->
            emit(
                Result.failure(throwable)
            )
        }


    override suspend fun saveMyComment(myComment: MyComment): Result<MyComment> {
        return try {
            val id = myCommentsDao.insertMyComment(myComment)
            Result.success(
                myComment.copy(id = id.toInt())
            )
        } catch (ex: Exception) {
            val error = Throwable(ex.message)
            Result.failure(error)
        }
    }

    override suspend fun deleteMyComment(commentId: Int): Result<Int> {
        return try {
            myCommentsDao.deleteMyComment(commentId)
            Result.success(0)
        } catch (ex: Exception) {
            val error = Throwable(ex.message)
            Result.failure(error)
        }
    }

}