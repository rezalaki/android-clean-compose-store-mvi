package com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.domain.usecase.brand.GetBrandsUseCase
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog.FiltersEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FiltersState())
    val state = _state
        .onStart { loadBrands(null) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FiltersState(isLoading = true)
        )

    private val _event = MutableSharedFlow<FiltersEvent>()
    val event = _event.asSharedFlow()

    fun handleIntent(intent: FiltersIntent) {
        viewModelScope.launch {
            when (intent) {
                is FiltersIntent.LoadFilters -> loadBrands(intent.filteringModel)

                FiltersIntent.ApplyFilters -> applyFilters()

                FiltersIntent.CloseDialog -> _event.emit(BackPressed)

                is FiltersIntent.SelectBrandItem -> selectBrandItem(intent.brand)

                is FiltersIntent.UnSelectBrandItem -> unSelectBrandItem(intent.brand)

                FiltersIntent.ResetFilters -> _event.emit(ResetFilterClicked)
            }
        }
    }

    private fun selectBrandItem(brand: Brand) {
        val updateList = _state.value.brandsList?.map { brandItem ->
            if (brandItem.id == brand.id)
                brandItem.copy(isSelected = true)
            else
                brandItem
        }
        _state.update {
            it.copy(
                brandsList = updateList as Set<Brand>?
            )
        }
    }

    private fun unSelectBrandItem(brand: Brand) {
        val updateList = _state.value.brandsList?.map { brandItem ->
            if (brandItem.id == brand.id)
                brandItem.copy(isSelected = false)
            else
                brandItem
        }
        _state.update {
            it.copy(
                brandsList = updateList as Set<Brand>?
            )
        }
    }

    private suspend fun applyFilters() {
        val selectedBrandIds = state.value.brandsList
            ?.filter { brand -> brand.isSelected }
            ?.map { brand -> brand.id }!!

        _event.emit(
            ApplyFilterClicked(
                FilteringModel(
                    brandIds = selectedBrandIds as Set<Int>?
                )
            )
        )
    }

    private suspend fun loadBrands(filteringModel: FilteringModel?) {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            getBrandsUseCase()
                .fold({ brandsList ->

                    val brands = brandsList.toMutableList()

                    with(filteringModel) {
                        if (this?.brandIds.isNullOrEmpty().not()) {
                            brands.filter { b -> b.id in brandIds }
                                .onEach {
                                    it.isSelected = true
                                }
                        }
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            brandsList = brands.toSet()
                        )
                    }
                }, { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message.orEmpty()
                        )
                    }
                })
        }
    }

}