package com.abhay.restaurantapp.data.api

import com.google.gson.annotations.SerializedName

// Response for get_item_list
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

data class Cuisine(
    @SerializedName("cuisine_id") val cuisineId: String,
    @SerializedName("cuisine_name") val cuisineName: String,
    @SerializedName("cuisine_image_url") val cuisineImageUrl: String,
    val items: List<MenuItem>
)

data class MenuItem(
    val id: String,
    val name: String,
    @SerializedName("image_url") val imageUrl: String,
    val price: String,
    val rating: String
)

// Response for get_item_by_id
data class ItemDetailResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("outcome_code") val outcomeCode: Int,
    @SerializedName("response_message") val responseMessage: String,
    @SerializedName("cuisine_id") val cuisineId: String,
    @SerializedName("cuisine_name") val cuisineName: String,
    @SerializedName("cuisine_image_url") val cuisineImageUrl: String,
    @SerializedName("item_id") val itemId: String,
    @SerializedName("item_name") val itemName: String,
    @SerializedName("item_price") val itemPrice: Double,
    @SerializedName("item_rating") val itemRating: Double,
    @SerializedName("item_image_url") val itemImageUrl: String
)

// Request and Response for get_item_by_filter
data class ItemFilterRequest(
    @SerializedName("cuisine_type") val cuisineType: List<String>? = null,
    @SerializedName("price_range") val priceRange: PriceRange? = null,
    @SerializedName("min_rating") val minRating: Double? = null
)

data class PriceRange(
    @SerializedName("min_amount") val minAmount: Double,
    @SerializedName("max_amount") val maxAmount: Double
)

// Request and Response for make_payment
data class PaymentRequest(
    @SerializedName("total_amount") val totalAmount: String,
    @SerializedName("total_items") val totalItems: Int,
    val data: List<PaymentItem>
)

data class PaymentItem(
    @SerializedName("cuisine_id") val cuisineId: Int,
    @SerializedName("item_id") val itemId: Int,
    @SerializedName("item_price") val itemPrice: Double,
    @SerializedName("item_quantity") val itemQuantity: Int
)

data class PaymentResponse(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("outcome_code") val outcomeCode: Int,
    @SerializedName("response_message") val responseMessage: String,
    @SerializedName("txn_ref_no") val transactionReferenceNumber: String
)