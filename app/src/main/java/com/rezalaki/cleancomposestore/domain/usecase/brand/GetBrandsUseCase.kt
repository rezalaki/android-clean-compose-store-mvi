package com.rezalaki.cleancomposestore.domain.usecase.brand

import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.domain.repository.BrandsRepository
import javax.inject.Inject


class GetBrandsUseCase @Inject constructor(
    private val brandsRepository: BrandsRepository
) {
    operator fun invoke(): Result<List<Brand>> {
        return brandsRepository.getBrands()
    }
}