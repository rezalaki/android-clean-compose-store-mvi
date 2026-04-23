package com.rezalaki.cleancomposestore.ui.screen.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.data.model.response.Product
import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
import com.rezalaki.cleancomposestore.ui.components.LoadingBox
import com.rezalaki.cleancomposestore.ui.screen.commentsdialog.CommentsBottomSheet
import com.rezalaki.cleancomposestore.ui.theme.grayBackground
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,
    onBackPressed: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel<DetailViewModel, DetailViewModelFactory> { factory ->
        factory.create(productId)
    }
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    val state by detailViewModel.state.collectAsStateWithLifecycle()
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            detailViewModel.handleIntents(DetailIntent.LoadProductDetail)

            detailViewModel.event.collectLatest { e ->
                when (e) {
                    DetailEvent.BackPressed -> {
                        onBackPressed.invoke()
                    }

                    is DetailEvent.OpenCommentsBottomSheet -> {
                        showBottomSheet = true
                    }

                    DetailEvent.CloseCommentsBottomSheet -> {
                        showBottomSheet = false
                    }
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopBar(productName = state.product?.title ?: "") {
                detailViewModel.handleIntents(DetailIntent.BackPressed)
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(grayBackground)
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoading ->
                        LoadingBox(
                            modifier = Modifier.fillMaxSize()
                        )

                    !state.error.isNullOrEmpty() ->
                        ErrorBoxWithRetryButton(
                            Modifier.fillMaxSize(),
                            state.error!!,
                            onBtnClicked = {
                                detailViewModel.handleIntents(DetailIntent.LoadProductDetail)
                            }
                        )

                    state.brand != null && state.product != null ->
                        DetailContent(
                            state.brand!!,
                            state.product!!,
                            commentButtonClicked = {
                                detailViewModel.handleIntents(
                                    DetailIntent.CommentsButtonPressed(productId)
                                )
                            }
                        )
                }
            }
            if (showBottomSheet) {
                CommentsBottomSheet(
                    productId = state.product!!.id,
                    onCloseCallBack = {
                        detailViewModel.handleIntents(DetailIntent.CloseCommentsBottomSheet)
                    }
                )
            }
        },
    )
}


@Composable
private fun DetailContent(
    brand: Brand,
    product: Product,
    commentButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(0.dp)
            .clip(
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = Color.White),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.image)
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.Center)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(brand.logoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .height(64.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.Black.copy(alpha = 0.3F))
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = Color.White),
    ) {
        Column {
            Text(
                text = "Details:",
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                color = Color.Gray
            )
            Text(
                text = product!!.detail,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 2.dp,
                    bottom = 8.dp
                )
            )
            Text(
                text = "Brand:",
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                color = Color.Gray
            )
            Text(
                text = brand!!.title,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 2.dp,
                    bottom = 10.dp
                )
            )
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, start = 16.dp, top = 24.dp)
            .height(48.dp),
        onClick = {
            commentButtonClicked.invoke()
        }
    ) {
        Text(text = "Comments")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(productName: String, onBackIconClicked: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
        ),
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    productName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}
