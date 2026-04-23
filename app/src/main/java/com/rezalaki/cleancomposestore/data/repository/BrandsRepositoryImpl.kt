package com.rezalaki.cleancomposestore.data.repository

import com.rezalaki.cleancomposestore.data.remote.ApiService
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.domain.repository.BrandsRepository
import javax.inject.Inject


class BrandsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BrandsRepository {

    override fun getBrands(): Result<List<Brand>> {
        return try {
            val response = apiService.getBrands().execute()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = Throwable(response.errorBody().toString())
                Result.failure(error)
            }
        } catch (ex: Exception) {
            val error = Throwable(ex.message)
            Result.failure(error)
        }
    }


    /**
     *  Because of using json files as mocked data for remote data source[apiService],
     *  we fetch all brands and then filter by `brandId` (which comes from argument) and
     *  return the found Brand info
     *
     * */
    override fun getBrandInfo(brandId: Int): Result<Brand> {
        return try {
            val response = apiService.getBrands().execute()
            if (response.isSuccessful && response.body() != null) {
                val brandInfo = response.body()!!
                    .find { brand -> brand.id == brandId }

                if (brandInfo != null) {
                    Result.success(brandInfo)
                } else {
                    val error = Throwable("No data found for [brandId = $brandId]")
                    Result.failure(error)
                }

            } else {
                val error = Throwable(response.errorBody().toString())
                Result.failure(error)
            }
        } catch (ex: Exception) {
            val error = Throwable(ex.message)
            Result.failure(error)
        }
    }

}