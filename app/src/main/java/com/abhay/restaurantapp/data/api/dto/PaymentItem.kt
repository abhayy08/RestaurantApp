package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class PaymentItem(
    @SerializedName("cuisine_id") val cuisineId: Int,
    @SerializedName("item_id") val itemId: Int,
    @SerializedName("item_price") val itemPrice: Double,
    @SerializedName("item_quantity") val itemQuantity: Int
)