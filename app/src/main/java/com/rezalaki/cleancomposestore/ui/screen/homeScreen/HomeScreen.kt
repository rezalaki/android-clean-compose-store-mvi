//package com.rezalaki.cleancomposestore.ui.screen.homeScreen
//
//
//import android.util.Log
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
//import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
//import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
//import androidx.compose.foundation.lazy.staggeredgrid.items
//import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.List
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.repeatOnLifecycle
//import coil.compose.AsyncImage
//import coil.request.ImageRequest
//import com.rezalaki.cleancomposestore.R
//import com.rezalaki.cleancomposestore.data.model.FilteringModel
//import com.rezalaki.cleancomposestore.data.model.response.Product
//import com.rezalaki.cleancomposestore.ui.components.ActionIconTypeEnum
//import com.rezalaki.cleancomposestore.ui.components.ConfirmDialog
//import com.rezalaki.cleancomposestore.ui.components.DebouncedTextField
//import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
//import com.rezalaki.cleancomposestore.ui.components.ImageDialog
//import com.rezalaki.cleancomposestore.ui.components.LoadingBox
//import com.rezalaki.cleancomposestore.ui.components.NewDebouncedTextFiled
//import com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog.FiltersDialog
//import com.rezalaki.cleancomposestore.ui.theme.grayBackground
//import kotlin.system.exitProcess
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    goToDetail: (productId: Int) -> Unit,
//    viewModel: HomeViewModel = hiltViewModel()
//) {
//    val state = viewModel.state.collectAsStateWithLifecycle()
//    val lifeCycleOwner = LocalLifecycleOwner.current
//
//    Log.d("TAGGGG", "HomeScreen: state -> ${state.value}")
//
//    BackHandler {
//        viewModel.handleIntents(HomeIntent.ShowExitDialog)
//    }
//
//
//    LaunchedEffect(Unit) {
//        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//            viewModel.handleIntents(
//                HomeIntent.LoadProducts(state.value.currentFilterModel)
//            )
//
//            viewModel.event.collect { e ->
//                when (e) {
//                    is HomeEvent.GoToDetails -> goToDetail.invoke(e.productId)
//                }
//            }
//        }
//    }
//
//    HomeScreenZ(viewModel.state.value){
//
//        viewModel.handleIntents(it)
//    }
//
//
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreenZ(
//    state: HomeState,
//    intentZ: (HomeIntent)-> Unit,
//    //goToDetail: (productId: Int) -> Unit
//) {
////    val state = viewModel.state.collectAsStateWithLifecycle()
////    val lifeCycleOwner = LocalLifecycleOwner.current
//
//
//
//
//
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            HomeTopBar(
//                title = "Products",
//                productViewType = state.listViewType,
//                onUpdateViewTypeClicked = {
////                    viewModel.handleIntents(
////                        HomeIntent.UpdateListViewType
////                    )
//
//                    intentZ.invoke(HomeIntent.UpdateListViewType)
//                }
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    //viewModel.handleIntents(HomeIntent.ShowFiltersDialog)
//                    intentZ.invoke(HomeIntent.ShowFiltersDialog)
//                }
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_filter),
//                    contentDescription = "filter"
//                )
//            }
//        },
//        content = { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .background(grayBackground)
//            ) {
//
//                NewDebouncedTextFiled(
//                    actionIconType = ActionIconTypeEnum.CLEAR,
//                    actionIconClicked = {
//                        intentZ.invoke(HomeIntent.UpdateSearchbarInput(""))
//                    },
//                    enable = true,
//                    hintText = "product titles...",
//                    showSearchIcon = true,
//                    debounceMillis = 500L,
//                    value = state.inputSearchbar,
//                    modifier = Modifier.fillMaxWidth().padding(16.dp),
//                    onValueChange = {
//                        intentZ.invoke(HomeIntent.UpdateSearchbarInput(it))
//                    },
//                    onDebouncedValueChange = {
//                        intentZ.invoke( HomeIntent.SearchProductName(it))
//                    },
//                )
//
///*
//                DebouncedTextField(
//                    inputString = state.inputSearchbar,
//                    onTextChange = { txt ->
//                        //viewModel.handleIntents(
//                        //    HomeIntent.UpdateSearchbarInput(txt)
//                        //)
//                        intentZ.invoke(HomeIntent.UpdateSearchbarInput(txt))
//                    },
//                    hintText = "product title...",
//                    onTextChangeAfterDelay = { query ->
//                        if (query.orEmpty().length > 3)
//                            //viewModel.handleIntents(
//                            //    HomeIntent.SearchProductName(query.orEmpty())
//                            //)
//                            intentZ.invoke( HomeIntent.SearchProductName(query.orEmpty()))
//                    },
//                    delayMillis = 500,
//                    asideIcon = Icons.Default.Close,
//                    onAsideIconClicked = {
//                        //viewModel.handleIntents(HomeIntent.UpdateSearchbarInput(""))
//                        //viewModel.handleIntents(HomeIntent.LoadProducts(state.value.currentFilterModel))
//
//                        intentZ.invoke(HomeIntent.UpdateSearchbarInput(""))
//                        intentZ.invoke(HomeIntent.LoadProducts(state.currentFilterModel))
//                    },
//                    enabled = true
//                )
// */
//                when {
//                    state.isLoading -> LoadingBox(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues)
//                    )
//
//                    !state.error.isNullOrEmpty() -> ErrorBoxWithRetryButton(
//                        modifier = Modifier.fillMaxSize(),
//                        errorMessage = state.error!!,
//                        onBtnClicked = {
//                            //viewModel.handleIntents(
//                            //    HomeIntent.LoadProducts(state.value.currentFilterModel)
//                            //)
//                            intentZ.invoke(HomeIntent.LoadProducts(state.currentFilterModel))
//                        }
//                    )
//
//                    else -> ProductsArea(
//                        viewType = state.listViewType,
//                        products = state.products,
//                        onProductClicked = { product ->
//                            //viewModel.handleIntents(
//                            //    HomeIntent.ProductClicked(product.id)
//                            //)
//                            intentZ.invoke(HomeIntent.ProductClicked(product.id))
//                        }
//                    )
//                }
//            }
//
//            if (state.showFiltersDialog) {
//                FiltersDialog(
//                    currentFilter = state.currentFilterModel,
//                    onApplyFilter = { filterModel ->
//                        //viewModel.handleIntents(
//                        //    HomeIntent.LoadProducts(filterModel)
//                        //)
//                        intentZ.invoke(HomeIntent.LoadProducts(filterModel))
//                    },
//                    onResetFilters = {
//                        //viewModel.handleIntents(
//                        //    HomeIntent.LoadProducts(null)
//                        //)
//                        intentZ.invoke(HomeIntent.LoadProducts(null))
//                    },
//                    onClose = {
//                        //viewModel.handleIntents(HomeIntent.HideFiltersDialog)
//                        intentZ.invoke(HomeIntent.HideFiltersDialog)
//                    },
//                )
//            }
//
//        }
//    )
//
//    if (state.showExitConfirmDialog) {
//        ConfirmDialog(
//            "Sure to exit app?",
//            yesClicked = {
//                exitProcess(0)
//            },
//            noClicked = {
//                //viewModel.handleIntents(
//                //    HomeIntent.HideExitDialog
//                //)
//                intentZ.invoke(HomeIntent.HideExitDialog)
//            }
//        )
//    }
//
//
//}
//
//
//@Composable
//fun HomeScreenContent(
//    state: HomeState,
//    inputTextChange: (String) -> Unit,
//    changeProductViewTypeClicked: () -> Unit,
//    fabClicked: () -> Unit,
//    confirmExitNoClicked: () -> Unit,
//    filterDialogApplyClicked: ( FilteringModel) -> Unit,
//    filterDialogResetClicked: () -> Unit,
//    filterDialogClose: () -> Unit,
//    loadProducts: (FilteringModel?) -> Unit,
//    productItemCLicked: (Product) -> Unit,
//) {
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            HomeTopBar(
//                title = "Products",
//                productViewType = state.listViewType,
//                onUpdateViewTypeClicked = changeProductViewTypeClicked
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = fabClicked
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_filter),
//                    contentDescription = "filter"
//                )
//            }
//        },
//        content = { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .background(grayBackground)
//            ) {
//                DebouncedTextField(
//                    inputString = state.inputSearchbar,
//                    onTextChange = inputTextChange,
//                    hintText = "product title...",
//                    onTextChangeAfterDelay = { query ->
//                        if (query.orEmpty().length > 3)
//                            inputTextChange.invoke(query.orEmpty())
//                    },
//                    delayMillis = 500,
//                    asideIcon = Icons.Default.Close,
//                    onAsideIconClicked = {
//                        inputTextChange.invoke("")
//                        loadProducts.invoke(state.currentFilterModel)
//                    },
//                    enabled = true
//                )
//                when {
//                    state.isLoading -> LoadingBox(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues)
//                    )
//
//                    !state.error.isNullOrEmpty() -> ErrorBoxWithRetryButton(
//                        modifier = Modifier.fillMaxSize(),
//                        errorMessage = state.error,
//                        onBtnClicked = {
//                            loadProducts.invoke(state.currentFilterModel)
//                        }
//                    )
//
//                    else -> ProductsArea(
//                        viewType = state.listViewType,
//                        products = state.products,
//                        onProductClicked = productItemCLicked
//                    )
//                }
//            }
//
//            if (state.showFiltersDialog) {
//                FiltersDialog(
//                    currentFilter = state.currentFilterModel,
//                    onApplyFilter = filterDialogApplyClicked,
//                    onResetFilters = filterDialogResetClicked,
//                    onClose = filterDialogClose,
//                )
//            }
//
//        }
//    )
//
//    if (state.showExitConfirmDialog) {
//        ConfirmDialog(
//            "Sure to exit app?",
//            yesClicked = {
//                exitProcess(0)
//            },
//            noClicked = confirmExitNoClicked
//        )
//    }
//
//}
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun HomeTopBar(
//    title: String,
//    productViewType: ProductViewType,
//    onUpdateViewTypeClicked: () -> Unit
//) {
//    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//        ), title = {
//            Text(
//                title, fontWeight = FontWeight.Bold
//            )
//        }, actions = {
//            IconButton(
//                onClick = {
//                    onUpdateViewTypeClicked.invoke()
//                }
//            ) {
//                Icon(
//                    imageVector = when (productViewType) {
//                        ProductViewType.GRID -> Icons.Filled.DateRange
//                        ProductViewType.LIST -> Icons.Filled.List
//                    },
//                    contentDescription = "view type"
//                )
//            }
//        }
//    )
//}
//
//
//@Composable
//private fun ProductsWrapper(
//    products: List<Product>,
//    viewType: ProductViewType,
//    gridListState: LazyStaggeredGridState,
//    verticalListState: LazyListState,
//    onProductClicked: (Product) -> Unit,
//    onImageClicked: (Product) -> Unit
//) {
//    when (viewType) {
//        ProductViewType.GRID -> ProductsListGrid(
//            gridListState,
//            products,
//            viewType,
//            onProductClicked,
//            onImageClicked
//        )
//
//        ProductViewType.LIST -> ProductsListVertical(
//            verticalListState,
//            products,
//            viewType,
//            onProductClicked,
//            onImageClicked
//        )
//    }
//}
//
//
//@Composable
//private fun ProductsListGrid(
//    gridState: LazyStaggeredGridState,
//    products: List<Product>,
//    viewType: ProductViewType,
//    onProductItemClicked: (Product) -> Unit,
//    onImageClicked: (Product) -> Unit
//) {
//    LazyVerticalStaggeredGrid(
//        columns = StaggeredGridCells.Fixed(2),
//        modifier = Modifier
//            .background(grayBackground)
//            .padding(horizontal = 4.dp),
//        state = gridState,
//        verticalItemSpacing = 0.dp,
//        horizontalArrangement = Arrangement.spacedBy(0.dp),
//        content = {
//            items(
//                items = products,
//                key = { p -> p.id }
//            ) { product ->
//                ProductItem(
//                    viewType,
//                    product = product,
//                    onProductImageClicked = {
//                        onImageClicked.invoke(product)
//                    },
//                    onProductRowClicked = {
//                        onProductItemClicked.invoke(product)
//                    }
//                )
//            }
//        }
//    )
//}
//
//
//@Composable
//private fun ProductsListVertical(
//    listState: LazyListState,
//    products: List<Product>,
//    viewType: ProductViewType,
//    onProductItemClicked: (Product) -> Unit,
//    onImageClicked: (Product) -> Unit
//) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 4.dp)
//            .background(grayBackground),
//        state = listState,
//    ) {
//        items(
//            items = products,
//            key = { p -> p.id },
//        ) { product ->
//            ProductItem(
//                viewType,
//                product = product,
//                onProductRowClicked = {
//                    onProductItemClicked.invoke(product)
//                },
//                onProductImageClicked = {
//                    onImageClicked.invoke(product)
//                }
//            )
//        }
//    }
//}
//
//
//@Composable
//private fun ProductsArea(
//    viewType: ProductViewType,
//    products: List<Product>,
//    onProductClicked: (Product) -> Unit
//) {
//    var imageProductDialog by remember { mutableStateOf<Product?>(null) }
//    val gridListState = rememberLazyStaggeredGridState()
//    val verticalListState = rememberLazyListState()
//
//    if (products.isEmpty()) {
//        Text(
//            "No data found",
//            fontSize = 23.sp,
//            modifier = Modifier
//                .fillMaxSize()
//                .wrapContentHeight(align = Alignment.CenterVertically)
//                .padding(horizontal = 24.dp),
//            textAlign = TextAlign.Center
//        )
//
//    } else {
//        ProductsWrapper(
//            products,
//            viewType,
//            gridListState,
//            verticalListState,
//            onProductClicked,
//            onImageClicked = { productItem ->
//                imageProductDialog = productItem
//            }
//        )
//
//        imageProductDialog?.let { product ->
//            ImageDialog(
//                imageUrl = product.image,
//                caption = product.title,
//                onDismissed = {
//                    imageProductDialog = null
//                }
//            )
//        }
//    }
//
//}
//
//
//@Composable
//private fun ProductItem(
//    viewType: ProductViewType,
//    product: Product,
//    onProductRowClicked: () -> Unit,
//    onProductImageClicked: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(0.dp)
//            .padding(
//                vertical = 10.dp,
//                horizontal = 12.dp
//            ),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White
//        ),
//        shape = RoundedCornerShape(8.dp),
//        onClick = {
//            onProductRowClicked.invoke()
//        }
//    ) {
//
//        when (viewType) {
//            ProductViewType.GRID -> {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    AsyncImage(
//                        model = ImageRequest.Builder(LocalContext.current).data(product.image)
//                            .crossfade(true).build(),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .clickable(onClick = {
//                                onProductImageClicked.invoke()
//                            }),
//                    )
//                    Text(
//                        text = product.title,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 14.sp,
//                        lineHeight = 14.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(horizontal = 16.dp, 10.dp)
//                    )
//                }
//            }
//
//            ProductViewType.LIST -> {
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    AsyncImage(
//                        model = ImageRequest.Builder(LocalContext.current).data(product.image)
//                            .crossfade(true).build(),
//                        placeholder = painterResource(R.drawable.ic_image_placeholder),
//                        error = painterResource(R.drawable.ic_error),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .fillMaxWidth(0.6F)
//                            .padding(10.dp)
//                            .clickable(
//                                onClick = {
//                                    onProductImageClicked.invoke()
//                                }
//                            ),
//                    )
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .fillMaxWidth(0.9F)
//                            .padding(end = 12.dp),
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = product.title,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 14.sp,
//                            lineHeight = 14.sp
//                        )
//                        HorizontalDivider(thickness = 8.dp, color = Color.Transparent)
//                        Text(text = product.detail, fontSize = 12.sp, lineHeight = 14.sp)
//                    }
//                    Image(
//                        imageVector = Icons.Default.KeyboardArrowRight,
//                        contentDescription = "",
//                        modifier = Modifier.size(32.dp),
//                        colorFilter = ColorFilter.tint(color = Color.LightGray)
//                    )
//                }
//            }
//        }
//
//    }
//
//}
//
//
////@Preview(showBackground = false)
////@Composable
////private fun ProductItemPreview() {
////    Box(
////        modifier = Modifier
////            .fillMaxWidth()
////            .height(80.dp)
////    ) {
////        ProductItem(
////            ProductViewType.LIST,
////            Product(0, 1, "iPhone 13", "512GB - 8GB", "fake_image_url"),
////            onProductRowClicked = {},
////            onProductImageClicked = {}
////        )
////    }
////}
//
//
//@Preview(showBackground = false)
//@Composable
//private fun ProductItemPreview() {
//
//    val state = HomeState(
//       isLoading   = false,
//       products   = listOf(
//           Product(1, 1, "asus n580vd", "ram 16GB - cpu i7", "https://dkstatics-public.digikala.com/digikala-products/5fb7326252e0d6f9f6463b05f047497314be7caa_1724415233.jpg?x-oss-process=image/resize,m_lfit,h_800,w_800/format,webp/quality,q_90"),
//           Product(2, 2, "Sony Xperia", "ram 4GB - 256GB", "https://dkstatics-public.digikala.com/digikala-products/5af568b4909435c6a215ad5e728e902db234b673_1736597759.jpg?x-oss-process=image/resize,m_lfit,h_800,w_800/format,webp/quality,q_90"),
//       ),
//       error   = null,
//       listViewType   =  ProductViewType.GRID,
//       showFiltersDialog   = false,
//       currentFilterModel   = null,
//       showExitConfirmDialog   = false,
//       inputSearchbar   = "",
//    )
//
//    HomeScreenZ(state) { }
//}
