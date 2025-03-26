package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class Cuisine(
    @SerializedName("cuisine_id") val cuisineId: String,
    @SerializedName("cuisine_name") val cuisineName: String,
    @SerializedName("cuisine_image_url") val cuisineImageUrl: String,
    val items: List<MenuItem>
)