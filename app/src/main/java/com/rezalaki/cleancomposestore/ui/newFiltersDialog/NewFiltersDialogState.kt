package com.rezalaki.cleancomposestore.ui.newFiltersDialog

import androidx.compose.runtime.Stable
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.data.model.response.Brand


@Stable
data class NewFiltersDialogState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val brandsList: Set<Brand>? = null
)

sealed class NewFiltersDialogIntent {
    data class BrandItemClicked(val brand: Brand) : NewFiltersDialogIntent()

    data object CloseClicked : NewFiltersDialogIntent()
    data object LoadBrands : NewFiltersDialogIntent()

    data object Submit : NewFiltersDialogIntent()
}

sealed interface NewFiltersDialogEffect {
    data object Close : NewFiltersDialogEffect
    data class Submit(val filterModel: FilteringModel) : NewFiltersDialogEffect
}