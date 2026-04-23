package com.rezalaki.cleancomposestore.data.repository

import com.rezalaki.cleancomposestore.data.remote.ApiService
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.domain.repository.ProductsRepository
import javax.inject.Inject


class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductsRepository {

    override fun getProducts(): Result<List<Product>> {
        val products = apiService.getProducts().execute()
        return if (products.isSuccessful && products.body() != null) {
            Result.success(products.body()!!)
        } else {
            val error = Throwable(products.errorBody().toString())
            Result.failure(error)
        }
    }



    /**
     *  Because of using json files as mocked data for remote data source[apiService],
     *  we fetch all products and then filter by `productId`
     *  (which comes from argument) and return the found Product info
     *
     * */
    override fun getProductInfo(productId: Int): Result<Product> {
        return try {
            val productsList = apiService.getProducts().execute()
            if (productsList.isSuccessful && productsList.body() != null) {
                val productInfo = productsList.body()!!
                    .find { product -> product.id == productId }

                if (productInfo != null) {
                    Result.success(productInfo)
                } else {
                    val error = Throwable("No info found for [productId = $productId]")
                    Result.failure(error)
                }
            } else {
                val error = Throwable(productsList.errorBody().toString())
                Result.failure(error)
            }

        } catch (ex: Exception) {
            val error = Throwable(ex.message)
            Result.failure(error)
        }
    }

}