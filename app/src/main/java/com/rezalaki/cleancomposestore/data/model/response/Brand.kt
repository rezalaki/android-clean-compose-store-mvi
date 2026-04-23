package com.rezalaki.cleancomposestore.data.model.response


import androidx.annotation.Keep

@Keep
data class Brand(
    val id: Int, // 1
    val title: String, // Samsung
    val logoUrl: String, // https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Samsung_Logo.svg/250px-Samsung_Logo.svg.png,
    var isSelected: Boolean = false
) {
    override fun toString(): String {
        return "{$id-$title-$isSelected}"
    }
}