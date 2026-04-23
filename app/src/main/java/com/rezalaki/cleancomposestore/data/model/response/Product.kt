package com.rezalaki.cleancomposestore.data.model.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Product(
    val id: Int, // 500
    @Expose @SerializedName("brand_id")
    val brandId: Int, // 1
    val title: String, // Samsung Galaxy S25 Ultra Dual SIM
    val detail: String, // 256GB And 12GB RAM Mobile Phone - Vietnam Pack
    val image: String // https://dkstatics-public.digikala.com/digikala-pro…esize,m_lfit,h_800,w_800/format,webp/quality,q_90
) {
    override fun toString(): String {
        return id.toString() + " - " + title + " - " + brandId.toString()
    }
}