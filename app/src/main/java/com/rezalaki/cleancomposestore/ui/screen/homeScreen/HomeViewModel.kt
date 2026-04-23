//package com.rezalaki.cleancomposestore.ui.screen.homeScreen
//
//import androidx.compose.runtime.collectAsState
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.rezalaki.cleancomposestore.data.model.FilteringModel
//import com.rezalaki.cleancomposestore.domain.usecase.product.GetProductsUseCase
//import com.rezalaki.cleancomposestore.ui.screen.homeScreen.HomeEvent.GoToDetails
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.flatMapConcat
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val getProductsUseCase: GetProductsUseCase
//) : ViewModel() {
//
//    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
//        _state.update {
//            it.copy(
//                isLoading = false,
//                error = "Error happened in Coroutine!"
//            )
//        }
//    }
//
//    private val _state = MutableStateFlow(HomeState())
//    val state = _state
//        .onStart {  }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = HomeState(isLoading = true)
//        )
//
//    private val _event =  MutableSharedFlow<HomeEvent>()
//    val event = _event.asSharedFlow()
//
//
//    fun handleIntents(intent: HomeIntent) {
//        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
//            when (intent) {
//                is HomeIntent.ProductClicked -> _event.emit(GoToDetails(intent.productId))
//
//                is HomeIntent.LoadProducts -> getProducts(intent.filterModel)
//
//                is HomeIntent.UpdateListViewType -> updateProductItemViewType()
//
//                is HomeIntent.SearchProductName -> searchProductName(intent.query)
//
//                HomeIntent.HideFiltersDialog -> _state.update {
//                    it.copy(showFiltersDialog = false)
//                }
//
//                HomeIntent.ShowFiltersDialog -> _state.update {
//                    it.copy(showFiltersDialog = true)
//                }
//
//                HomeIntent.HideExitDialog -> _state.update {
//                    it.copy(showExitConfirmDialog = false)
//                }
//
//                HomeIntent.ShowExitDialog -> _state.update {
//                    it.copy(showExitConfirmDialog = true)
//                }
//
//                is HomeIntent.UpdateSearchbarInput -> _state.update {
//                    it.copy(inputSearchbar = intent.text)
//                }
//            }
//        }
//    }
//
//    private suspend fun getProducts(filteringModel: FilteringModel?) {
//        _state.update {
//            it.copy(
//                isLoading = true
//            )
//        }
//
//        getProductsUseCase(filteringModel)
//            .fold({ products ->
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        products = products,
//                        error = null
//                    )
//                }
//            }, {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        error = "failed to fetch data"
//                    )
//                }
//            })
//    }
//
//    private fun updateProductItemViewType() {
//        val newViewType = when (_state.value.listViewType) {
//            ProductViewType.GRID -> ProductViewType.LIST
//            ProductViewType.LIST -> ProductViewType.GRID
//        }
//        _state.update {
//            it.copy(listViewType = newViewType)
//        }
//    }
//
//    private suspend fun searchProductName(query: String) {
//        withContext(Dispatchers.IO) {
//            _state.update {
//                it.copy( isLoading = true )
//            }
//            delay(1_000)
//            val searchedProducts = _state.value.products
//                .filter { p ->
//                    p.title.lowercase().contains(query)
//                }
//            _state.update {
//                it.copy(
//                    products = searchedProducts,
//                    isLoading = false,
//                )
//            }
//        }
//    }
//
//
//}