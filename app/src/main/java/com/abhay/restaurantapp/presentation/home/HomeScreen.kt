package com.abhay.restaurantapp.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.abhay.restaurantapp.R
import com.abhay.restaurantapp.data.api.Cuisine
import com.abhay.restaurantapp.data.api.MenuItem
import com.abhay.restaurantapp.presentation.common.DishItem

@Composable
fun HomeScreen(
    uiState: UiState = UiState(),
    onCuisineClick: (String, String) -> Unit = {id, name -> },
    onAddItem: (MenuItem) -> Unit = {},
    onRemoveItem: (MenuItem) -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
    clearError: () -> Unit,
    getMoreCuisines: () -> Unit
) {

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            onShowSnackbar(if(uiState.error.isEmpty()) "Something went wrong" else uiState.error)
            clearError()
        }
    }

    val lazyListState = rememberLazyListState()
    val isLastElementVisible = lazyListState.let {
        derivedStateOf { it.layoutInfo.visibleItemsInfo.lastOrNull()?.index == it.layoutInfo.totalItemsCount - 1 }.value
    }

    LaunchedEffect(isLastElementVisible) {
        if(isLastElementVisible) {
            getMoreCuisines()
        }
    }


    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        if(uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Text(
                    text = stringResource(R.string.cuisines), style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    state = lazyListState
                ) {
                    items(uiState.cuisine) {
                        CuisineCard(
                            cuisine = it,
                            onCuisineClick = onCuisineClick
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(bottom = 45.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.top_3_dishes), style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    if(!uiState.isTopItemsLoading){
                        uiState.topItems.forEach {menuItem ->
                            val item = uiState.cart.find { it.item.id == menuItem.id }
                            DishItem(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                dish = menuItem,
                                onAddItem = onAddItem,
                                onRemoveItem = onRemoveItem,
                                currentCountInCart = item?.quantity ?: 0
                            )
                        }
                    }else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CuisineCard(
    modifier: Modifier = Modifier,
    cuisine: Cuisine,
    onCuisineClick: (String, String) -> Unit = {id, name -> }
) {
    Card(
        modifier = modifier
            .padding(6.dp)
            .width(500.dp)
            .height(200.dp)
            .clickable { onCuisineClick(cuisine.cuisineId, cuisine.cuisineName) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = rememberAsyncImagePainter(cuisine.cuisineImageUrl),
                contentDescription = cuisine.cuisineName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = cuisine.cuisineName,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
