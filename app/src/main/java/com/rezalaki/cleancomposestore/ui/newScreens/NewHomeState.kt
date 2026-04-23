package com.rezalaki.cleancomposestore.ui.newScreens

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.ProductViewType

@Stable
data class NewHomeState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val listViewType: ProductViewType = ProductViewType.LIST,
    val showFiltersDialog: Boolean = false,
    val currentFilterModel: FilteringModel? = null,
    val showExitConfirmDialog: Boolean = false,
    val inputSearchbar: String = "",
    val showProductImageDialog: Product? = null,
)

sealed class NewHomeIntent {
    data class LoadProducts(val query: String?, val filterModel: FilteringModel?) : NewHomeIntent()
    data class UpdateSearchbarInput(val text: String) : NewHomeIntent()

    object UpdateListViewType : NewHomeIntent()

    data class GoToDetails(val productId: Int) : NewHomeIntent()

    data class ShowImageDialog(val product: Product) : NewHomeIntent()
    object HideImageDialog : NewHomeIntent()

    object ShowFiltersDialog : NewHomeIntent()
    object HideFiltersDialog : NewHomeIntent()

    object ShowExitDialog : NewHomeIntent()
    object HideExitDialog : NewHomeIntent()

    object ExitApplication: NewHomeIntent()
}

sealed interface NewHomeEffect {
    data class GoToDetailScreen(val productId: Int) : NewHomeEffect
    object ExitApp : NewHomeEffect
}