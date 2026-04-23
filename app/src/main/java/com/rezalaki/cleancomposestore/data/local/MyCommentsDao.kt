package com.rezalaki.cleancomposestore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rezalaki.cleancomposestore.data.model.db.MyComment


@Dao
interface MyCommentsDao {

    @Insert
    suspend fun insertMyComment(myComment: MyComment): Long

    @Update
    suspend fun updateMyComment(myComment: MyComment)

    @Query("SELECT * FROM my_comments WHERE productId=:productId; ")
    suspend fun getMyComments(productId: Int): List<MyComment>

    @Query("DELETE FROM my_comments WHERE id =:commentId; ")
    suspend fun deleteMyComment(commentId: Int)

}