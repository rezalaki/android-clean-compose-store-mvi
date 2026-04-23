package com.rezalaki.cleancomposestore.ui.screen.homeScreen

import com.rezalaki.cleancomposestore.ui.screen.homeScreen.ProductViewType.GRID
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.ProductViewType.LIST

enum class ProductViewType {
    GRID,
    LIST,
}

fun ProductViewType.toggle(): ProductViewType {
    return when (this) {
        GRID -> LIST
        LIST -> GRID
    }
}