package com.rezalaki.cleancomposestore.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.utils.Constants


@Entity(tableName = "my_comments")
data class MyComment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val body: String,
    val productId: Int
)

/**
 *
 * Local comments, known as [MyComment] entity, are stored without [User] entity and [Comment] entity contains [User].
 * So this method convert the [MyComment] to [Comment] by setting [User], which is a static variable in [Constants.ME]
 *
 * */
fun MyComment.toComment() =
    Comment(
        id = id,
        body = body,
        productId = productId,
        user = Constants.ME
    )

fun MyComment.isValid(): Result<Unit> {
    val isLongEnough = body.length > 10
    val hasSwearWords =
        body.contains("sex") ||
                body.contains("dick") ||
                body.contains("fuck") ||
                body.contains("ass")

    return if (isLongEnough) {
        Result.failure(Throwable("comment text length must be more than 10 characters!"))

    } else if (hasSwearWords) {
        Result.failure(Throwable("comment must NOT contain bad word!"))

    } else {
        Result.success(Unit)
    }
}

