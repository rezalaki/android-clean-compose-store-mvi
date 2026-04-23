package com.rezalaki.cleancomposestore.domain.repository

import com.rezalaki.cleancomposestore.data.model.response.Product

interface ProductsRepository {
    fun getProducts(): Result<List<Product>>
    fun getProductInfo(productId: Int): Result<Product>
}