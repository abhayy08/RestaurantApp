package com.abhay.restaurantapp.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.restaurantapp.data.api.Cuisine
import com.abhay.restaurantapp.data.api.MenuItem
import com.abhay.restaurantapp.domain.CartItem
import com.abhay.restaurantapp.domain.FoodRepository
import com.abhay.restaurantapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        getItemList()
        getTopItems()
    }

    private var page = 1
    private var count = 10

    private fun getItemList() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val resource = foodRepository.getItemList(1, 10)
            page += 2
            when (resource) {
                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            cuisine = resource.data!!.cuisines, isLoading = false
                        )
                    }
                }

                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            error = resource.message, isLoading = false
                        )
                    }
                }

            }
            Log.d("HomeViewModel", "getItemList: ${uiState.value}")
        }
    }

    fun getMoreCuisines() {
        viewModelScope.launch(Dispatchers.IO) {
            val resource = foodRepository.getItemList(page, count)
            page += 2

            when (resource) {
                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            cuisine = it.cuisine + resource.data!!.cuisines
                        )
                    }
                }

                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            error = resource.message
                        )
                    }
                }
            }

            Log.d("HomeViewModel", "getItemList: ${uiState.value}")
        }
    }

    private fun getTopItems() {
        _uiState.update { it.copy(isTopItemsLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val resource = foodRepository.getItemByFilter(null, null, 4.0)
            when (resource) {
                is Resource.Success<*> -> {
                    val topItems = getTopItemsInDetail(resource.data!!.cuisines[0].items.take(3))
                    _uiState.update {
                        it.copy(
                            topItems = topItems,
                            cuisineIdofTopItems = resource.data.cuisines[0].cuisineId,
                            isTopItemsLoading = false
                        )
                    }
                }

                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            error = resource.message, isTopItemsLoading = false
                        )
                    }
                }

            }
            Log.d("HomeViewModel", "getTopItems:ItemList: ${uiState.value.topItems}")
        }
    }

    suspend fun getTopItemsInDetail(items: List<MenuItem>): MutableList<MenuItem> {
        val resultList = mutableListOf<MenuItem>()
        for (item in items) {
            val resource = foodRepository.getItemById(item.id.toInt())
            when (resource) {
                is Resource.Success<*> -> {
                    resource.data?.let { itemDetail ->
                        resultList.add(
                            MenuItem(
                                id = itemDetail.itemId,
                                name = itemDetail.itemName,
                                imageUrl = itemDetail.itemImageUrl,
                                price = itemDetail.itemPrice.toString(),
                                rating = itemDetail.itemRating.toString()
                            )
                        )
                    }
                }

                is Resource.Error<*> -> {
                    _uiState.update { it.copy(error = resource.message) }
                }
            }
        }
        return resultList
    }

    fun addItemToCart(menuItem: MenuItem, cuisineId: String = "") {
        val currentCart = _uiState.value.cart.toMutableList()
        val existingCartItemIndex = currentCart.indexOfFirst { it.item.id == menuItem.id }

        Log.d("HomeViewModel", "addItemToCart: $currentCart")
        if (existingCartItemIndex != -1) {
            val existingCartItem = currentCart[existingCartItemIndex]
            currentCart[existingCartItemIndex] =
                existingCartItem.copy(quantity = existingCartItem.quantity + 1)
        } else {
            currentCart.add(CartItem(item = menuItem, cuisineId = cuisineId, quantity = 1))
        }
        _uiState.update { it.copy(cart = currentCart) }
    }

    fun removeItemFromCart(menuItem: MenuItem) {
        val currentCart = _uiState.value.cart.toMutableList()
        val existingCartItemIndex = currentCart.indexOfFirst { it.item.id == menuItem.id }

        Log.d("HomeViewModel", "removeItemFromCart: $currentCart")
        if (existingCartItemIndex != -1) {
            val existingCartItem = currentCart[existingCartItemIndex]

            if (existingCartItem.quantity == 1) {
                currentCart.removeAt(existingCartItemIndex)
            } else {
                currentCart[existingCartItemIndex] =
                    existingCartItem.copy(quantity = existingCartItem.quantity - 1)
            }
            _uiState.update { it.copy(cart = currentCart) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}

data class UiState(
    val cuisine: List<Cuisine> = emptyList(),
    val topItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val isTopItemsLoading: Boolean = false,
    val error: String? = null,
    val cart: List<CartItem> = emptyList(),
    val cuisineIdofTopItems: String = ""
)