package com.rezalaki.cleancomposestore.ui.newFiltersDialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.domain.usecase.brand.GetBrandsUseCase
import com.rezalaki.cleancomposestore.ui.newScreens.NewHomeEffect
import com.rezalaki.cleancomposestore.ui.screen.commentsdialog.CommentsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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


@AssistedFactory
interface NewFiltersDialogViewModelFactory {
    fun create(currentFilterModel: FilteringModel?): NewFiltersDialogViewModel
}

/*
*
*   Check lifeCycle of dialogs in jetpack compose,
*   Why state is kept?!
*
* */

@HiltViewModel(assistedFactory = NewFiltersDialogViewModelFactory::class)
class NewFiltersDialogViewModel @AssistedInject constructor(
    @Assisted private var currentFilterModel: FilteringModel?,
    private val getBrandsUseCase: GetBrandsUseCase
) : ViewModel() {

    init {
        Log.d("TAGGGG", "NewFiltersDialogViewModel init : $currentFilterModel")
    }

    private val _state = MutableStateFlow(NewFiltersDialogState())
    val state = _state
        .onStart { getBrands() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewFiltersDialogState(isLoading = true)
        )

    private val _effect = MutableSharedFlow<NewFiltersDialogEffect>()
    val effect = _effect.asSharedFlow()

    fun initTheVM(filterModel: FilteringModel?) {
        currentFilterModel = filterModel
    }

    fun handleIntent(intent: NewFiltersDialogIntent) {
        viewModelScope.launch {
            when (intent) {
                is NewFiltersDialogIntent.BrandItemClicked -> {
                    val updatedBrandsList = _state.value.brandsList?.map {
                        if (it.id == intent.brand.id)
                            it.copy(isSelected = it.isSelected.not())
                        else
                            it
                    }
                    _state.update {
                        it.copy(
                            brandsList = updatedBrandsList?.toSet()
                        )
                    }
                }

                NewFiltersDialogIntent.CloseClicked -> {
                    _effect.emit(NewFiltersDialogEffect.Close)
                }

                NewFiltersDialogIntent.LoadBrands -> {
                    getBrands()
                }

                NewFiltersDialogIntent.Submit -> {
                    val selectedBrands =
                        _state.value.brandsList?.filter { it.isSelected }?.map { it.id }?.toSet()
                    val filterModel = FilteringModel(
                        brandIds = selectedBrands
                    )
                    _effect.emit(NewFiltersDialogEffect.Submit(filterModel))
                }
            }
        }
    }

    private suspend fun getBrands() {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                )
            }
            delay(1_000)
            getBrandsUseCase()
                .fold(
                    onSuccess = { brandList ->

                        // TODO dialog lifecycle about filterModel injecting !!?
                        // affect the current filter params to fetched brands
                        val currentFilterBrandIds =
                            currentFilterModel?.brandIds?.toList() ?: emptyList()
                        val updatedBrands = brandList.map { brand ->
                            if (brand.id in currentFilterBrandIds) {
                                brand.copy(isSelected = true)
                            } else {
                                brand.copy(isSelected = false)
                            }
                        }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                brandsList = updatedBrands.toSet()
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message ?: "Unknown Error"
                            )
                        }
                    })
        }
    }

}