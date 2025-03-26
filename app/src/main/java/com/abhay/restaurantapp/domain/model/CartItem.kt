package com.abhay.restaurantapp.domain.model

import com.abhay.restaurantapp.data.api.dto.MenuItem

data class CartItem(
    val item: MenuItem,
    val cuisineId: String,
    val quantity: Int
)