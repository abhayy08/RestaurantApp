package com.abhay.restaurantapp.presentation.cuisine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CuisineViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun initializeViewModel(cuisineId: String, cart: List<CartItem>) {
        getItemList(cuisineId)
        _uiState.update { it.copy(cart = cart) }
    }

    fun getItemList(cuisineId: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val resource = foodRepository.getItemList(1, 10)
            when (resource) {
                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            cuisineItems = resource.data!!.cuisines.find { it.cuisineId == cuisineId }!!.items,
                            isLoading = false
                        )
                    }
                    Log.d("CuisineViewModel", "getItemList: ${uiState.value}")
                }

                is Resource.Error<*> -> {
                    _uiState.update { it.copy(error = resource.message, isLoading = false) }
                }

                else -> {}
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