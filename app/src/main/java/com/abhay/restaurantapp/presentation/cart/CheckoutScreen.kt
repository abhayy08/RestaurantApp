package com.abhay.restaurantapp.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.abhay.restaurantapp.R
import com.abhay.restaurantapp.domain.model.CartItem

@Composable
fun CheckoutScreen(
    cartState: CartState,
    onCheckOut: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    clearError: () -> Unit
) {

    LaunchedEffect(cartState.error) {
        cartState.error?.let {
            onShowSnackbar(cartState.error)
            clearError()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            cartState.items.forEach { item ->
                CheckoutItem(
                    modifier = Modifier.padding(vertical = 4.dp), cartItem = item
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.total), modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Row(
                    modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.unique_items) + "${cartState.items.size}")
                    Text(
                        text = "₹${cartState.grandTotalAmount}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp), onClick = onCheckOut, shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = stringResource(R.string.order_now))
        }
        if (cartState.isPaymentLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun CheckoutItem(
    modifier: Modifier = Modifier, cartItem: CartItem
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.item.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "₹${cartItem.item.price}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
            }
            Row(
                modifier = Modifier.weight(0.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Q: ${cartItem.quantity}")
                Text(text = "${cartItem.item.price.toDouble() * cartItem.quantity}")
            }
        }
    }
}