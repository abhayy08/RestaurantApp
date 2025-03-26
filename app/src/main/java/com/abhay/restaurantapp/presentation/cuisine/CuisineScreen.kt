package com.abhay.restaurantapp.presentation.cuisine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.abhay.restaurantapp.data.api.MenuItem
import com.abhay.restaurantapp.presentation.common.DishItem

@Composable
fun CuisineScreen(
    state: UiState,
    onAddItem: (MenuItem) -> Unit = {},
    onRemoveItem: (MenuItem) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
    onPopBack: () -> Boolean
) {

    LaunchedEffect(state.error) {
        onShowSnackbar(state.error ?: "An unknow error occured")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            items(state.cuisineItems) {menuItem ->
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
