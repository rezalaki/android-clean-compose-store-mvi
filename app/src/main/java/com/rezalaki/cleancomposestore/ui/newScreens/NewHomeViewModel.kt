package com.rezalaki.cleancomposestore.ui.newScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.domain.usecase.product.GetProductsUseCase
import com.rezalaki.cleancomposestore.ui.newScreens.NewHomeEffect.*
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.ProductViewType
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.toggle
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
class NewHomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewHomeState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewHomeState(isLoading = true)
        )

    private val _effect = MutableSharedFlow<NewHomeEffect>()
    val effect = _effect.asSharedFlow()

    fun handleIntent(intent: NewHomeIntent) {
        Log.d("TAGGGG", "${this::class.simpleName} handleIntent(): $intent")
        viewModelScope.launch {
            when (intent) {
                NewHomeIntent.HideFiltersDialog -> {
                    _state.update {
                        it.copy(showFiltersDialog = false)
                    }
                }

                NewHomeIntent.ShowFiltersDialog -> {
                    _state.update {
                        it.copy(showFiltersDialog = true)
                    }
                }

                is NewHomeIntent.LoadProducts -> loadProducts(intent.query, intent.filterModel)

                NewHomeIntent.ShowExitDialog -> {
                    _state.update {
                        it.copy(showExitConfirmDialog = true)
                    }
                }

                NewHomeIntent.HideExitDialog -> {
                    _state.update {
                        it.copy(showExitConfirmDialog = false)
                    }
                }

                NewHomeIntent.UpdateListViewType -> {
                    _state.update {
                        it.copy(listViewType = it.listViewType.toggle())
                    }
                }

                is NewHomeIntent.UpdateSearchbarInput -> {
                    _state.update {
                        it.copy(inputSearchbar = intent.text)
                    }
                }

                NewHomeIntent.HideImageDialog -> {
                    _state.update {
                        it.copy(showProductImageDialog = null)
                    }
                }

                is NewHomeIntent.ShowImageDialog -> {
                    _state.update {
                        it.copy(showProductImageDialog = intent.product)
                    }
                }

                is NewHomeIntent.GoToDetails -> _effect.emit(
                    GoToDetailScreen(intent.productId)
                )

                NewHomeIntent.ExitApplication -> _effect.emit(NewHomeEffect.ExitApp)
            }
        }
    }

    private suspend fun loadProducts(query: String?, filteringModel: FilteringModel?) {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(isLoading = true, currentFilterModel = filteringModel)
            }
            getProductsUseCase.invoke(query, filteringModel)
                .fold(
                    onSuccess = { productsList ->

                        _state.update {
                            it.copy(
                                products = productsList,
                                isLoading = false,
                                error = null
                            )
                        }

                    },
                    onFailure = { throwable ->

                        _state.update {
                            it.copy(
                                products = emptyList(),
                                isLoading = false,
                                error = "something went wrong :/"
                            )
                        }

                    }
                )
        }
    }

}