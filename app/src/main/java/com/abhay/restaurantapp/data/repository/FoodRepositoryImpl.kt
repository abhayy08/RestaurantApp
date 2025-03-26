package com.abhay.restaurantapp.data.repository

import android.util.Log
import com.abhay.restaurantapp.data.api.FoodServiceApi
import com.abhay.restaurantapp.data.api.ItemDetailResponse
import com.abhay.restaurantapp.data.api.ItemFilterRequest
import com.abhay.restaurantapp.data.api.ItemListResponse
import com.abhay.restaurantapp.data.api.PaymentItem
import com.abhay.restaurantapp.data.api.PaymentRequest
import com.abhay.restaurantapp.data.api.PaymentResponse
import com.abhay.restaurantapp.data.api.PriceRange
import com.abhay.restaurantapp.domain.FoodRepository
import com.abhay.restaurantapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodServiceApi: FoodServiceApi
) : FoodRepository {
    override suspend fun getItemList(page: Int, count: Int): Resource<ItemListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = foodServiceApi.getItemList(mapOf("page" to page, "count" to count))
                Log.d("FoodRepository", "getItemList: ${handleResponse(response).data}")
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun getItemById(itemId: Int): Resource<ItemDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = foodServiceApi.getItemById(mapOf("item_id" to itemId))
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun getItemByFilter(
        cuisineType: List<String>?, priceRange: PriceRange?, minRating: Double?
    ): Resource<ItemListResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ItemFilterRequest(cuisineType, priceRange, minRating)
                val response = foodServiceApi.getItemByFilter(request)
                Log.d(
                    "FoodRepository",
                    "getItemByFilter: ${handleResponse(response).data!!.cuisines[0].items.take(3)}"
                )
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun makePayment(
        totalAmount: String, totalItems: Int, paymentItems: List<PaymentItem>
    ): Resource<PaymentResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = PaymentRequest(totalAmount, totalItems, paymentItems)
                val response = foodServiceApi.makePayment(request)
                handleResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(response.message())
        }
    }
}