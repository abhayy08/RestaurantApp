package com.abhay.restaurantapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class ItemListResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("outcome_code") val outcomeCode: Int,
    @SerializedName("response_message") val responseMessage: String,
    val page: Int,
    val count: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_items") val totalItems: Int,
    val cuisines: List<Cuisine>
)
