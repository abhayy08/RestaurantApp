package com.abhay.restaurantapp.presentation.cuisine

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhay.restaurantapp.data.api.dto.MenuItem
import com.abhay.restaurantapp.presentation.common.DishItem

@Composable
fun CuisineScreen(
    state: UiState,
    onAddItem: (MenuItem) -> Unit,
    onRemoveItem: (MenuItem) -> Unit,
    onShowSnackbar: (String) -> Unit,
    clearError: () -> Unit,
) {
    LaunchedEffect(state.error) {
        state.error?.let {
            onShowSnackbar(it)
            clearError()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(bottom = 60.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(state.cuisineItems, key = { it.id }) { menuItem ->
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
}
