package com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.data.model.FilteringModel


@Stable
data class FiltersState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val brandsList: Set<Brand>? = null
)

sealed class FiltersIntent {
    data class LoadFilters(val filteringModel: FilteringModel?) : FiltersIntent()
    object ResetFilters : FiltersIntent()
    object ApplyFilters : FiltersIntent()
    object CloseDialog : FiltersIntent()

    data class SelectBrandItem(val brand: Brand) : FiltersIntent()
    data class UnSelectBrandItem(val brand: Brand) : FiltersIntent()
}


sealed class FiltersEvent {
    object BackPressed : FiltersEvent()
    data class ApplyFilterClicked(val filterModel: FilteringModel) : FiltersEvent()
    object ResetFilterClicked : FiltersEvent()
}
