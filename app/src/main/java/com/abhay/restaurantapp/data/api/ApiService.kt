package com.abhay.restaurantapp.data.api

import com.abhay.restaurantapp.data.api.dto.ItemDetailResponse
import com.abhay.restaurantapp.data.api.dto.ItemFilterRequest
import com.abhay.restaurantapp.data.api.dto.ItemListResponse
import com.abhay.restaurantapp.data.api.dto.PaymentRequest
import com.abhay.restaurantapp.data.api.dto.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FoodServiceApi {
    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_list",
        "Content-Type: application/json"
    )
    @POST("/emulator/interview/get_item_list")
    suspend fun getItemList(
        @Body request: Map<String, Int>
    ): Response<ItemListResponse>

    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_by_id",
        "Content-Type: application/json"
    )
    @POST("/emulator/interview/get_item_by_id")
    suspend fun getItemById(
        @Body request: Map<String, Int>
    ): Response<ItemDetailResponse>

    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_by_filter",
        "Content-Type: application/json"
    )
    @POST("/emulator/interview/get_item_by_filter")
    suspend fun getItemByFilter(
        @Body request: ItemFilterRequest
    ): Response<ItemListResponse>

    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: make_payment",
        "Content-Type: application/json"
    )
    @POST("/emulator/interview/make_payment")
    suspend fun makePayment(
        @Body request: PaymentRequest
    ): Response<PaymentResponse>
}