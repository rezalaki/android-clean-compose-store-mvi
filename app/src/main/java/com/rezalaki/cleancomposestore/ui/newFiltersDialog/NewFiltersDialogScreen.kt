package com.rezalaki.cleancomposestore.ui.newFiltersDialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.rezalaki.cleancomposestore.R
import com.rezalaki.cleancomposestore.data.model.FilteringModel
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
import com.rezalaki.cleancomposestore.ui.components.LoadingBox
import com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog.FiltersIntent


@Preview
@Composable
private fun DialogPreview() {
//    NewFiltersDialog()
}


@Composable
fun NewFiltersDialog(
    currentFilteringModel: FilteringModel?,
    viewModel: NewFiltersDialogViewModel = hiltViewModel<NewFiltersDialogViewModel, NewFiltersDialogViewModelFactory> { factory ->
        factory.create(currentFilteringModel)
    },
    onSubmit: (FilteringModel?) -> Unit,
    onClose: () -> Unit,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

            viewModel.initTheVM(filterModel = currentFilteringModel)

            viewModel.effect.collect { eff ->
                when (eff) {
                    NewFiltersDialogEffect.Close -> onClose.invoke()
                    is NewFiltersDialogEffect.Submit -> {
                        onSubmit.invoke(eff.filterModel)
                        onClose.invoke()
                    }
                }
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(NewFiltersDialogIntent.CloseClicked)
    }

    val state = viewModel.state.collectAsStateWithLifecycle()
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false
        )
    ) {
        NewFiltersDialogContent(
            state = state.value,
            intent = viewModel::handleIntent
        )
    }
}


@Composable
private fun NewFiltersDialogContent(
    state: NewFiltersDialogState,
    intent: (NewFiltersDialogIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Filters",
                modifier = Modifier.weight(1F),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            IconButton(
                onClick = {
                    intent.invoke(NewFiltersDialogIntent.CloseClicked)
                }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Gray.copy(alpha = 0.8F)
                )
            }
        }
        when {
            state.isLoading -> {
                LoadingBox(
                    modifier = Modifier.fillMaxSize()
                )
            }

            !state.error.isNullOrEmpty() -> {
                ErrorBoxWithRetryButton(
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = state.error,
                    onBtnClicked = {
                        intent.invoke(NewFiltersDialogIntent.LoadBrands)
                    }
                )
            }

            !state.brandsList.isNullOrEmpty() -> {
/////////////////////////////////////////////////////////// Brands
                BrandsListBox(
                    brandsList = state.brandsList.toList(),
                    brandItemClicked = { brand ->
                        intent.invoke(
                            NewFiltersDialogIntent.BrandItemClicked(brand)
                        )
                    }
                )
/////////////////////////////////////////////////////////// Others
                Text("Other Filters:", fontSize = 20.sp)
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color.LightGray.copy(alpha = 0.6F),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    "<< not developed yet >>",
                    modifier = Modifier
                        .padding(0.dp)
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Red.copy(alpha = 0.8F)
                )
/////////////////////////////////////////////////////////// Submit Button
                Spacer(Modifier.weight(1F))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .requiredHeight(48.dp),
                    onClick = {
                        intent.invoke(NewFiltersDialogIntent.Submit)
                    }
                ) {
                    Text("Submit Filters")
                }
            }
        }
    }
}


@Composable
private fun BrandsListBox(
    brandsList: List<Brand>,
    brandItemClicked: (Brand) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text("Brands:", fontSize = 20.sp)
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray.copy(alpha = 0.6F),
            modifier = Modifier.padding(top = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            content = {
                items(
                    items = brandsList,
                    key = { brand -> brand.id }
                ) { brandItem: Brand ->
                    BrandItem(
                        brand = brandItem,
                        onChecked = {
                            brandItemClicked.invoke(brandItem)
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun BrandItem(
    brand: Brand,
    onChecked: (Boolean) -> Unit
) {
    val icon = if (brand.isSelected) painterResource(R.drawable.ic_check_box_filled)
    else painterResource(R.drawable.ic_check_box_empty)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                brand.isSelected.let {
                    onChecked.invoke(!it)
                }
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = "click", modifier = Modifier.padding(start = 16.dp))
        Text(
            brand.title,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        )
    }
}
