package com.abhay.restaurantapp.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.restaurantapp.domain.CartItem
import com.abhay.restaurantapp.domain.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    fun initializeCart(cartItems: List<CartItem>) {
        _cartState.value = _cartState.value.copy(items = cartItems)
        calculateTotalAmount()
    }

    fun calculateTotalAmount() {
        _cartState.value = _cartState.value.copy(isLoading = true)
        val cartItems = _cartState.value.items
        var netTotal = 0.0
        cartItems.forEach { (menuItem, _, quantity) ->
            netTotal += menuItem.price.toDouble() * quantity
        }
        val cgst = netTotal * 0.025
        val sgst = netTotal * 0.025
        val grandTotal = netTotal + cgst + sgst

        _cartState.value = _cartState.value.copy(
            netTotalAmount = netTotal,
            grandTotalAmount = grandTotal,
            isLoading = false
        )
    }

    fun checkout() {
        viewModelScope.launch {

        }
    }
}


data class CartState(
    val netTotalAmount: Double = 0.0,
    val grandTotalAmount: Double = 0.0,
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPaymentLoading: Boolean = false
)