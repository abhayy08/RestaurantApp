package com.abhay.restaurantapp.presentation.cuisine

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhay.restaurantapp.data.api.MenuItem
import com.abhay.restaurantapp.presentation.common.DishItem

@Composable
fun CuisineScreen(
    state: UiState,
    onAddItem: (MenuItem) -> Unit,
    onRemoveItem: (MenuItem) -> Unit,
    onShowSnackbar: (String) -> Unit,
    clearError: () -> Unit,
    getMoreItems: () -> Unit
) {

    LaunchedEffect(state.error) {
        state.error?.let {
            onShowSnackbar(state.error)
            clearError()
        }
    }

    val lazyListState = rememberLazyListState()
    val isLastElementVisible = lazyListState.let {
        derivedStateOf { it.layoutInfo.visibleItemsInfo.lastOrNull()?.index == it.layoutInfo.totalItemsCount - 1 }.value
    }

    LaunchedEffect(isLastElementVisible) {
        if(isLastElementVisible) {
            getMoreItems()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            state = lazyListState
        ) {
            items(state.cuisineItems) { menuItem ->
                val item = state.cart?.find { it.item.id == menuItem.id }
                DishItem(
                    dish = menuItem,
                    onAddItem = onAddItem,
                    onRemoveItem = onRemoveItem,
                    currentCountInCart = item?.quantity ?: 0
                )
            }
        }
    }
}
