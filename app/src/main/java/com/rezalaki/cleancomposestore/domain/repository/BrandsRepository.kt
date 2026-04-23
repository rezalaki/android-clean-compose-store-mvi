package com.rezalaki.cleancomposestore.domain.repository

import com.rezalaki.cleancomposestore.data.model.response.Brand


interface BrandsRepository {
    fun getBrands(): Result<List<Brand>>
    fun getBrandInfo(brandId: Int): Result<Brand>
}