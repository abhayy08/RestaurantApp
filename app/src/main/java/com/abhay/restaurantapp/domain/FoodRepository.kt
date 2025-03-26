package com.abhay.restaurantapp.domain

import com.abhay.restaurantapp.data.api.ItemDetailResponse
import com.abhay.restaurantapp.data.api.ItemListResponse
import com.abhay.restaurantapp.data.api.PaymentItem
import com.abhay.restaurantapp.data.api.PaymentResponse
import com.abhay.restaurantapp.data.api.PriceRange
import com.abhay.restaurantapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun getItemList(page: Int, count: Int): Resource<ItemListResponse>

    suspend fun getItemById(itemId: Int): Resource<ItemDetailResponse>

    suspend fun getItemByFilter(
        cuisineType: List<String>? = null,
        priceRange: PriceRange? = null,
        minRating: Double? = null
    ): Resource<ItemListResponse>

    suspend fun makePayment(
        totalAmount: String,
        totalItems: Int,
        paymentItems: List<PaymentItem>
    ): Resource<PaymentResponse>

}