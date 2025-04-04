package com.abhay.restaurantapp.presentation.cuisine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.restaurantapp.data.api.dto.MenuItem
import com.abhay.restaurantapp.data.api.dto.toMenuItem
import com.abhay.restaurantapp.domain.model.CartItem
import com.abhay.restaurantapp.domain.repository.FoodRepository
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
class CuisineViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun initializeViewModel(cuisineId: String, cuisineName: String, cart: List<CartItem>) {
        getItemList(cuisineId, cuisineName)
        _uiState.update { it.copy(cart = cart) }
    }

    fun getItemList(cuisineId: String, cuisineName: String) {
        if (_uiState.value.cuisineItems.isNotEmpty()) return

        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val resource = foodRepository.getItemByFilter(listOf(cuisineName), null, null)
            when (resource) {
                is Resource.Success -> {
                    val itemList = resource.data?.cuisines?.find { it.cuisineId == cuisineId }?.items ?: emptyList()

                    val detailedItems = itemList.mapNotNull { item ->
                        val itemDetail = foodRepository.getItemById(item.id.toInt())
                        if (itemDetail is Resource.Success) {
                            itemDetail.data?.toMenuItem()
                        } else null
                    }

                    _uiState.update { it.copy(cuisineItems = detailedItems, isLoading = false) }
                    Log.d("CuisineViewModel", "getItemList: ${uiState.value}")
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(error = resource.message, isLoading = false) }
                }
            }
        }
    }



    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}

data class UiState(
    val cuisineItems: List<MenuItem> = emptyList(),
    val error: String? = null,
    val cart: List<CartItem>? = null,
    val isLoading: Boolean = false
)