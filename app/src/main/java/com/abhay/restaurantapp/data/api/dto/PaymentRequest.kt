package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("total_amount") val totalAmount: String,
    @SerializedName("total_items") val totalItems: Int,
    val data: List<PaymentItem>
)