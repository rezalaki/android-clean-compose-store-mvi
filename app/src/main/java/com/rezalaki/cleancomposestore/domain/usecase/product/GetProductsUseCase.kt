package com.rezalaki.cleancomposestore.domain.usecase.product

import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.domain.repository.ProductsRepository
import javax.inject.Inject


/**
 * This class is used to load products from remote data source and also
 * apply filters and search query, if necessary.
 *
 * Because we are using static mock data for our remote data source,
 * we apply filtering by [FilteringModel] data class and [ProductFilterEngine].
 *
 **/
class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    operator fun invoke(query: String?, filterModel: FilteringModel?): Result<List<Product>> {
        return productsRepository.getProducts()
            .fold({ allProductsList ->

                val targetProducts = ProductFilterEngine(
                    productList = allProductsList,
                    filter = filterModel,
                    titleQuery = query
                ).applyFilters()

                Result.success(targetProducts)

            }, { throwable ->
                Result.failure(throwable)
            })
    }

    /**
     *
     * `ProductFilterEngine` is the class we isolate logic of filtering products inside.
     * At this time, we only covered filtering by Brand, but in future we will implement Colors,
     * GuaranteeIds, maximumPrice and minimumPrice too.
     * Also we handle searching for product based on its title in this class.
     *
     **/
    private class ProductFilterEngine(
        private val productList: List<Product>,
        private val filter: FilteringModel?,
        private val titleQuery: String?
    ) {

        fun applyFilters(): List<Product> = productList.filter { product ->
            matchTitle(product)
                    && matchBrands(product)
                // && matchColors(product)
                // && matchMaxPrice(product)
                // && matchMinPrice(product)
        }

        private fun matchTitle(product: Product): Boolean {
            return if (titleQuery.isNullOrEmpty())
                true
            else
                product.title
                    .trim()
                    .lowercase()
                    .contains(
                        titleQuery
                            .lowercase()
                            .trim()
                    )
        }

        private fun matchBrands(product: Product): Boolean =
            filter?.brandIds.isNullOrEmpty() || product.brandId in filter.brandIds

//        private fun matchColors(product: Product): Boolean =
//            filter.colorIds.isNullOrEmpty() || product.colorId in filter.colorIds

//        private fun matchMaxPrice(product: Product): Boolean =
//            filter.maxPrice == null || product.price <= filter.maxPrice

//        private fun matchMinPrice(product: Product): Boolean =
//            filter.minPrice == null || product.price >= filter.minPrice

    }

}
