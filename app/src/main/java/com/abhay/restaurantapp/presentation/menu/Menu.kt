package com.abhay.restaurantapp.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.abhay.restaurantapp.R
import com.abhay.restaurantapp.domain.CartItem

@Composable
fun MenuScreen(
    cart: List<CartItem>,
    onAddItem: (CartItem) -> Unit = {},
    onRemoveItem: (CartItem) -> Unit = {},
    onProceedToCheckout: () -> Unit = {},
    onPopBack: () -> Unit = {}
) {

    LaunchedEffect(cart.size) {
        if(cart.isEmpty()) {
            onPopBack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 60.dp),
        ) {
            cart.forEach { item ->
                MenuItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    item = item,
                    onAddItem = onAddItem,
                    onRemoveItem = onRemoveItem
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            onClick = onProceedToCheckout,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(text = stringResource(R.string.proceed_to_checkout))
        }
    }
}

@Composable
fun MenuItemCard(
    modifier: Modifier = Modifier,
    item: CartItem,
    onAddItem: (CartItem) -> Unit = {},
    onRemoveItem: (CartItem) -> Unit = {}
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Image(
                    painter = rememberAsyncImagePainter(item.item.imageUrl),
                    contentDescription = item.item.name,
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                Column (
                    modifier = Modifier.padding(4.dp)
                ){
                    Text(text = item.item.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = item.item.price, color = Color.DarkGray)
                }
            }

            MenuQuantityButton(
                dish = item,
                currentCountInCart = item.quantity,
                onAddItem = {
                    onAddItem(it)
                },
                onRemoveItem = {
                    onRemoveItem(it)
                }
            )
        }
    }
}

@Composable
fun MenuQuantityButton(
    modifier: Modifier = Modifier,
    dish: CartItem,
    currentCountInCart: Int,
    onAddItem: (CartItem) -> Unit,
    onRemoveItem: (CartItem) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onRemoveItem(dish) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Remove Item"
            )
        }
        Text(
            text = currentCountInCart.toString(),
            modifier = Modifier.padding(8.dp)
        )
        IconButton(
            onClick = { onAddItem(dish) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Remove Item"
            )
        }
    }
}