package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class ItemFilterRequest(
    @SerializedName("cuisine_type") val cuisineType: List<String>? = null,
    @SerializedName("price_range") val priceRange: PriceRange? = null,
    @SerializedName("min_rating") val minRating: Double? = null
)