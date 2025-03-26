package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class PriceRange(
    @SerializedName("min_amount") val minAmount: Double,
    @SerializedName("max_amount") val maxAmount: Double
)