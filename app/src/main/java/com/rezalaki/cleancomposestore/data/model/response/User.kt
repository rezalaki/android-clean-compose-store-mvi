package com.rezalaki.cleancomposestore.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class User(
    val id: Int, // 10043
    @Expose @SerializedName("first_name") val firstName: String, // James
    @Expose @SerializedName("last_name") val lastName: String // Johnson
) {
    fun getFullName() = "$firstName $lastName"
}