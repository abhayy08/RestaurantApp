package com.abhay.restaurantapp.domain.repository

import com.abhay.restaurantapp.data.api.dto.ItemDetailResponse
import com.abhay.restaurantapp.data.api.dto.ItemListResponse
import com.abhay.restaurantapp.data.api.dto.PaymentItem
import com.abhay.restaurantapp.data.api.dto.PaymentResponse
import com.abhay.restaurantapp.data.api.dto.PriceRange
import com.abhay.restaurantapp.utils.Resource

interface FoodRepository {

    suspend fun getItemList(page: Int, count: Int): Resource<ItemListResponse>

    suspend fun getItemById(itemId: Int): Resource<ItemDetailResponse>

    suspend fun getItemByFilter(
        cuisineType: List<String>? = null, priceRange: PriceRange? = null, minRating: Double? = null
    ): Resource<ItemListResponse>

    suspend fun makePayment(
        totalAmount: String, totalItems: Int, paymentItems: List<PaymentItem>
    ): Resource<PaymentResponse>

}