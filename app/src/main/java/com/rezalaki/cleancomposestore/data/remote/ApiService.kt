package com.rezalaki.cleancomposestore.data.remote

import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.data.model.response.Comment
import com.rezalaki.cleancomposestore.data.model.response.Product
import ir.logicbase.mockfit.Mock
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * ApiService is using mocked data provided by `MockFitInterceptor`, also see [com.rezalaki.cleancomposestore.di.ApiModule.provideMockFitInterceptor]
 *
 * All responses are single separated json files located in `fakeData` directory,
 *
 * `fakeData` directory is located in `assets`:
 * [assets/fakeData](app/src/main/assets/fakeData)
 *
 */
interface ApiService {

    @Mock("brands.json")
    @GET("brands")
    fun getBrands(): Call<List<Brand>>

    @Mock("products.json")
    @GET("products")
    fun getProducts(): Call<List<Product>>

    @Mock("comments.json")
    @GET("comments")
    fun getComments(@Query("product_id") productId: Int): Call<List<Comment>>

}