package com.abhay.restaurantapp.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.abhay.restaurantapp.R
import com.abhay.restaurantapp.data.api.MenuItem

@Composable
fun DishItem(
    modifier: Modifier = Modifier,
    dish: MenuItem,
    onAddItem: (MenuItem) -> Unit = {},
    onRemoveItem: (MenuItem) -> Unit = {},
    currentCountInCart: Int = 0
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(dish.imageUrl),
                contentDescription = dish.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .scale(1.2f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 14.dp)
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.5f)),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dish.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${dish.price}", style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "★ ${dish.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Yellow
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (currentCountInCart > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onRemoveItem(dish) }) {
                    Icon(
                        imageVector = Icons.Rounded.Remove, contentDescription = "Remove Item"
                    )
                }
                Text(
                    text = currentCountInCart.toString(), modifier = Modifier.padding(8.dp)
                )
                IconButton(
                    onClick = { onAddItem(dish) }) {
                    Icon(
                        imageVector = Icons.Rounded.Add, contentDescription = "Remove Item"
                    )
                }
            }
        } else {
            Button(
                modifier = Modifier.fillMaxWidth(), onClick = { onAddItem(dish) }) {
                Text(stringResource(R.string.add_item_to_cart))
            }
        }
    }
}