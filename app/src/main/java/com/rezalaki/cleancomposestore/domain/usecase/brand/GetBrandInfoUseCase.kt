package com.rezalaki.cleancomposestore.domain.usecase.brand

import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.domain.repository.BrandsRepository
import javax.inject.Inject


class GetBrandInfoUseCase @Inject constructor(
    private val brandsRepository: BrandsRepository
) {
     operator fun invoke(brandId: Int): Result<Brand>  {
        return brandsRepository.getBrandInfo(brandId)
    }
}
