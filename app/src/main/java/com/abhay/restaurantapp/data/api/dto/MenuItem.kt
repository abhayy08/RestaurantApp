package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class MenuItem(
    val id: String,
    val name: String,
    @SerializedName("image_url") val imageUrl: String,
    val price: String,
    val rating: String
)