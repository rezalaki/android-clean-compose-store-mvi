package com.rezalaki.cleancomposestore.domain.usecase.product

import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.domain.repository.ProductsRepository
import javax.inject.Inject


class GetProductInfoUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    operator fun invoke(productId: Int): Result<Product> {
        return productsRepository.getProductInfo(productId)
    }

}