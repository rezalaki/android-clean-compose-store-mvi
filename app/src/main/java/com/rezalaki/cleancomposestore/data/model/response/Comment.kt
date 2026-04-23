package com.rezalaki.cleancomposestore.data.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rezalaki.cleancomposestore.utils.Constants

@Keep
data class Comment(
    val id: Int, // 5043
    val user: User,
    val body: String, // Environment type response around
    @Expose @SerializedName("product_id") val productId: Int // 800
) {

    fun isMine(): Boolean {
        return user.id == Constants.ME.id
    }

}