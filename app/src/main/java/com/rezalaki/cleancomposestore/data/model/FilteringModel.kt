package com.rezalaki.cleancomposestore.data.model


/**
 *   `FilteringModel` data class is used to hold our filters we use in ViewModels and Repositories.
 *
 *   At this time we have implement only filtering by set of `brands`
 *
 *   In future we will implement filtering by set of `colors`, `guarantees`, minimum and maximum `price` and etc.
 *   This also needs changes in UI, logics and other related parts
 *
 * */
data class FilteringModel(
    val brandIds: Set<Int>?,
//    val colorIds: Set<Int>?,
//    val guaranteeIds: Set<Int>?
//    val maxPrice: BigDecimal?
//    val minPrice: BigDecimal?
)