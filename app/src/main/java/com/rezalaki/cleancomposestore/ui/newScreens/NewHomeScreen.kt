package com.rezalaki.cleancomposestore.ui.newScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rezalaki.cleancomposestore.R
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.ui.components.ActionIconTypeEnum
import com.rezalaki.cleancomposestore.ui.components.ConfirmDialog
import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
import com.rezalaki.cleancomposestore.ui.components.ImageDialog
import com.rezalaki.cleancomposestore.ui.components.LoadingBox
import com.rezalaki.cleancomposestore.ui.components.NewDebouncedTextFiled
import com.rezalaki.cleancomposestore.ui.newFiltersDialog.NewFiltersDialog
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.ProductViewType
import com.rezalaki.cleancomposestore.ui.theme.grayBackground
import kotlin.system.exitProcess


@Composable
fun NewHomeScreen(
    goToDetail: (productId: Int) -> Unit,
    viewModel: NewHomeViewModel = hiltViewModel()
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { eff ->
                when (eff) {
                    is NewHomeEffect.GoToDetailScreen -> goToDetail.invoke(eff.productId)
                    NewHomeEffect.ExitApp -> exitProcess(0)
                }
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(NewHomeIntent.ShowExitDialog)
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    NewHomeScreenContent(
        state = state.value,
        handleIntent = viewModel::handleIntent
    )

}

@Composable
private fun NewHomeScreenContent(
    state: NewHomeState,
    handleIntent: (NewHomeIntent) -> Unit
) {
    val gridListState = rememberLazyStaggeredGridState()
    val verticalListState = rememberLazyListState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeTopBar(
                title = "Products",
                productViewType = state.listViewType,
                onUpdateViewTypeClicked = {
                    handleIntent.invoke(NewHomeIntent.UpdateListViewType)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "filter"
                    )
                },
                onClick = {
                    handleIntent.invoke(NewHomeIntent.ShowFiltersDialog)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(grayBackground)
        ) {
            NewDebouncedTextFiled(
                actionIconType = ActionIconTypeEnum.CLEAR,
                actionIconClicked = {
                    handleIntent.invoke(NewHomeIntent.UpdateSearchbarInput(""))
                },
                enable = true,
                hintText = "product titles...",
                showSearchIcon = true,
                debounceMillis = 500L,
                value = state.inputSearchbar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onValueChange = { txt ->
                    handleIntent.invoke(NewHomeIntent.UpdateSearchbarInput(txt))
                },
                onDebouncedValueChange = { txt ->
                    handleIntent.invoke(NewHomeIntent.LoadProducts(txt, state.currentFilterModel))
                },
            )
            when {
                state.isLoading -> LoadingBox(
                    modifier = Modifier
                        .fillMaxSize()
                )

                state.error.isNullOrEmpty().not() -> ErrorBoxWithRetryButton(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow),
                    errorMessage = "sample Error",
                    onBtnClicked = {
                        handleIntent.invoke(
                            NewHomeIntent.LoadProducts(
                                state.inputSearchbar,
                                state.currentFilterModel
                            )
                        )
                    }
                )

                state.products.isEmpty() -> Text(
                    "No data found",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center
                )

                state.products.isNotEmpty() -> {
                    when (state.listViewType) {
                        ProductViewType.GRID -> LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .background(grayBackground)
                                .padding(horizontal = 4.dp),
                            verticalItemSpacing = 0.dp,
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            state = gridListState,
                            content = {
                                items(
                                    items = state.products,
                                    key = { it.id },
                                    contentType = { "product_item" }
                                ) { product ->
                                    ProductGridItem(
                                        product,
                                        imgClicked = {
                                            handleIntent.invoke(
                                                NewHomeIntent.ShowImageDialog(
                                                    product
                                                )
                                            )
                                        },
                                        itemClicked = {
                                            handleIntent.invoke(NewHomeIntent.GoToDetails(product.id))
                                        }
                                    )
                                }
                            }
                        )

                        ProductViewType.LIST -> LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp)
                                .background(grayBackground),
                            state = verticalListState
                        ) {
                            items(
                                items = state.products,
                                key = { it.id },
                                contentType = { "product_item" }
                            ) { product ->
                                ProductVerticalItem(
                                    product,
                                    imgClicked = {
                                        handleIntent.invoke(NewHomeIntent.ShowImageDialog(product))
                                    },
                                    itemClicked = {
                                        handleIntent.invoke(NewHomeIntent.GoToDetails(product.id))
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }
    }

    if (state.showExitConfirmDialog) {
        ConfirmDialog(
            message = "sure to exit the app?",
            dismissOnBackPress = false,
            yesClicked = {
                handleIntent.invoke(NewHomeIntent.ExitApplication)
            },
            noClicked = {
                handleIntent.invoke(NewHomeIntent.HideExitDialog)
            }
        )
    }

    if (state.showProductImageDialog != null) {
        ImageDialog(
            imageUrl = state.showProductImageDialog.image,
            caption = state.showProductImageDialog.title,
            onDismissed = {
                handleIntent.invoke(NewHomeIntent.HideImageDialog)
            }
        )
    }

    if (state.showFiltersDialog) {
        NewFiltersDialog(
            currentFilteringModel = state.currentFilterModel,
            onSubmit = { filterModel ->
                Log.d("TAGGGG", "dialog | submit -> $filterModel")
                handleIntent.invoke(NewHomeIntent.LoadProducts(null, filterModel))
            },
            onClose = {
                handleIntent.invoke(NewHomeIntent.HideFiltersDialog)
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun NewHomeScreenContentPreview() {
    NewHomeScreenContent(
        state = NewHomeState(
            isLoading = true
        ),
        handleIntent = {}
    )
}


@Composable
private fun ProductGridItem(
    product: Product,
    imgClicked: () -> Unit,
    itemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(
                vertical = 10.dp,
                horizontal = 12.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        onClick = itemClicked::invoke
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = imgClicked::invoke),
            )
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, 10.dp)
            )
        }
    }
}


@Composable
private fun ProductVerticalItem(
    product: Product,
    imgClicked: () -> Unit,
    itemClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(
                vertical = 10.dp,
                horizontal = 12.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        onClick = itemClicked::invoke
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_image_placeholder),
                error = painterResource(R.drawable.ic_error),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxWidth(0.6F)
                    .padding(10.dp)
                    .clickable(
                        onClick = imgClicked::invoke
                    ),
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9F)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
                HorizontalDivider(thickness = 8.dp, color = Color.Transparent)
                Text(text = product.detail, fontSize = 12.sp, lineHeight = 14.sp)
            }
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(color = Color.LightGray)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    title: String,
    productViewType: ProductViewType,
    onUpdateViewTypeClicked: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                title, fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onUpdateViewTypeClicked.invoke()
                }
            ) {
                Icon(
                    imageVector = when (productViewType) {
                        ProductViewType.GRID -> Icons.Filled.DateRange
                        ProductViewType.LIST -> Icons.Filled.List
                    },
                    contentDescription = "view type"
                )
            }
        }
    )
}
