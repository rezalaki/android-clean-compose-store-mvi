package com.rezalaki.cleancomposestore.ui.screen.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezalaki.cleancomposestore.domain.usecase.brand.GetBrandInfoUseCase
import com.rezalaki.cleancomposestore.domain.usecase.product.GetProductInfoUseCase
import com.rezalaki.cleancomposestore.ui.screen.detailscreen.DetailEvent.OpenCommentsBottomSheet
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



@AssistedFactory
interface DetailViewModelFactory {
    fun create(productId: Int): DetailViewModel
}

@HiltViewModel(assistedFactory = DetailViewModelFactory::class)
class DetailViewModel @AssistedInject constructor(
    @Assisted private val productId: Int,
    private val productInfoUseCase: GetProductInfoUseCase,
    private val brandInfoUseCase: GetBrandInfoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailState(isLoading = true)
        )

    private val _event = MutableSharedFlow<DetailEvent>()
    val event = _event.asSharedFlow()

    fun handleIntents(intent: DetailIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                DetailIntent.BackPressed -> _event.emit(DetailEvent.BackPressed)

                is DetailIntent.CommentsButtonPressed -> _event.emit( OpenCommentsBottomSheet(intent.productId) )

                is DetailIntent.LoadProductDetail -> loadProductInfo()

                DetailIntent.CloseCommentsBottomSheet -> _event.emit( DetailEvent.CloseCommentsBottomSheet )
            }
        }
    }

    private suspend fun loadProductInfo() {
        withContext(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val productInfo = productInfoUseCase(productId).getOrThrow()
                val brandDeferred = async { brandInfoUseCase(productInfo.brandId).getOrThrow() }
                val brandInfo = brandDeferred.await()

                _state.update {
                    it.copy(
                        product = productInfo,
                        brand = brandInfo,
                        error = null,
                        isLoading = false
                    )
                }

            } catch (ex: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "failed to load needed data | ${ex.message}"
                    )
                }
            }

        }
    }

}