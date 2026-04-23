package com.rezalaki.cleancomposestore.ui.screen.homeScreen.filtersDialog


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.rezalaki.cleancomposestore.data.model.response.Brand
import com.rezalaki.cleancomposestore.ui.components.ErrorBoxWithRetryButton
import com.rezalaki.cleancomposestore.ui.components.LoadingBox
import com.rezalaki.cleancomposestore.R
import com.rezalaki.cleancomposestore.data.model.FilteringModel


@Composable
fun FiltersDialog(
    currentFilter: FilteringModel?,
    onApplyFilter: (FilteringModel) -> Unit,
    onResetFilters: () -> Unit,
    onClose: () -> Unit,
    filtersViewModel: FiltersViewModel = hiltViewModel()
) {
    val state = filtersViewModel.state.collectAsStateWithLifecycle()
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            filtersViewModel.handleIntent(FiltersIntent.LoadFilters(currentFilter))

            filtersViewModel.event.collect { e ->
                when (e) {
                    is FiltersEvent.ApplyFilterClicked -> {
                        onApplyFilter.invoke(e.filterModel)
                        onClose.invoke()
                    }

                    is FiltersEvent.BackPressed -> {
                        onClose.invoke()
                    }

                    FiltersEvent.ResetFilterClicked -> {
                        onResetFilters.invoke()
                        onClose.invoke()
                    }
                }
            }
        }
    }

    Dialog(
        onDismissRequest = {
            onClose.invoke()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        FiltersDialogContent(
            state = state.value,
            currentFilter = currentFilter,
            onClose = onClose,
            filtersViewModel
        )
    }

}


@Composable
private fun FiltersDialogContent(
    state: FiltersState,
    currentFilter: FilteringModel?,
    onClose: () -> Unit,
    filtersViewModel: FiltersViewModel
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
                .padding(0.dp),
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
                    onClose.invoke()
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
        HorizontalDivider(thickness = 32.dp, color = Color.Transparent)
        when {
            state.isLoading -> LoadingBox(
                modifier = Modifier.fillMaxSize()
            )

            !state.error.isNullOrEmpty() -> ErrorBoxWithRetryButton(
                modifier = Modifier.fillMaxSize(),
                errorMessage = state.error,
                onBtnClicked = {
                    filtersViewModel.handleIntent(FiltersIntent.LoadFilters(currentFilter))
                }
            )

            !state.brandsList.isNullOrEmpty() -> {
                BrandsListBox(
                    brandsList = state.brandsList.toList(),
                    brandItemClicked = { clickedBrand ->
                        val intent = if (clickedBrand.isSelected == true)
                            FiltersIntent.UnSelectBrandItem(clickedBrand)
                        else
                            FiltersIntent.SelectBrandItem(clickedBrand)
                        filtersViewModel.handleIntent(intent)
                    }
                )
                // ============== other filter params that are NOT developed yet!
                // other filter params can be such as: Colors, Sizes, Guarantees, ...
                HorizontalDivider(thickness = 32.dp, color = Color.Transparent)
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
                    color = Color.Red.copy(alpha = 0.5F)
                )
                // ============== Buttons in bottom of dialog
                Spacer(Modifier.weight(1F))
                ApplyAndRestButtonsBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(bottom = 16.dp),
                    applyFiltersButtonClicked = {
                        filtersViewModel.handleIntent(FiltersIntent.ApplyFilters)
                    },
                    resetButtonClicked = {
                        filtersViewModel.handleIntent(FiltersIntent.ResetFilters)
                    }
                )
            }
        }
    }

}


@Composable
private fun ApplyAndRestButtonsBox(
    modifier: Modifier,
    applyFiltersButtonClicked: () -> Unit,
    resetButtonClicked: () -> Unit
) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier
                .weight(1F)
                .padding(end = 8.dp)
                .fillMaxHeight(),
            onClick = applyFiltersButtonClicked
        ) {
            Text("apply")
        }
        Button(
            modifier = Modifier
                .weight(1F)
                .padding(start = 8.dp)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            onClick = resetButtonClicked
        ) {
            Text("Reset", color = Color.Black)
        }
    }
}


@Composable
private fun BrandsListBox(
    brandsList: List<Brand>,
    brandItemClicked: (Brand) -> Unit
) {
    Column {
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
                    key = { b -> b.id }
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


@Preview(showBackground = true)
@Composable
private fun BrandsListBoxPreview() {
    val brands = listOf(
        Brand(1, "Samsung", "", false),
        Brand(2, "Sony", "", true),
        Brand(3, "Apple", "", false),
        Brand(4, "LG", "", false),
    )
    BrandsListBox(brands) {

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
        Icon(icon, contentDescription = null, modifier = Modifier.padding(start = 16.dp))
        Text(
            brand.title,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BrandItemPreview() {
    val sample = Brand(1, "Samsung Sample", "", true)
    BrandItem(brand = sample, onChecked = {})
}

