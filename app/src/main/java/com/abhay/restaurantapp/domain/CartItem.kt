package com.abhay.restaurantapp.domain

import com.abhay.restaurantapp.data.api.Cuisine
import com.abhay.restaurantapp.data.api.MenuItem

data class CartItem(
    val item: MenuItem,
    val cuisineId: String,
    val quantity: Int
)