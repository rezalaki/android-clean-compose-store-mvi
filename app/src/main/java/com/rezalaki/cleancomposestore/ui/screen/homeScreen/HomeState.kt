package com.rezalaki.cleancomposestore.ui.screen.homeScreen

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.data.model.FilteringModel


@Stable
data class HomeState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val listViewType: ProductViewType = ProductViewType.LIST,
    val showFiltersDialog: Boolean = false,
    val currentFilterModel: FilteringModel? = null,
    val showExitConfirmDialog: Boolean = false,
    val inputSearchbar: String = "",
)

sealed class HomeIntent {
    data class SearchProductName(val query: String) : HomeIntent()
    data class ProductClicked(val productId: Int) : HomeIntent()
    data class LoadProducts(val filterModel: FilteringModel?) : HomeIntent()
    object UpdateListViewType : HomeIntent()
    data class UpdateSearchbarInput(val text: String) : HomeIntent()

    object ShowFiltersDialog : HomeIntent()
    object HideFiltersDialog : HomeIntent()

    object ShowExitDialog : HomeIntent()
    object HideExitDialog : HomeIntent()
}

sealed interface HomeEvent {
    class GoToDetails(val productId: Int) : HomeEvent
}
