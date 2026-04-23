package com.rezalaki.cleancomposestore.ui.screen.detailscreen

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.data.model.response.Product


@Stable
data class DetailState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val brand: Brand? = null,
    val error: String? = null
)

sealed interface DetailIntent {
    object BackPressed : DetailIntent
    object LoadProductDetail : DetailIntent
    data class CommentsButtonPressed(val productId: Int) : DetailIntent
    object CloseCommentsBottomSheet : DetailIntent
}


sealed class DetailEvent {
    object BackPressed : DetailEvent()
    data class OpenCommentsBottomSheet(val productId: Int) : DetailEvent()
    object CloseCommentsBottomSheet : DetailEvent()
}