package com.abhay.restaurantapp.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhay.restaurantapp.data.api.PaymentItem
import com.abhay.restaurantapp.domain.CartItem
import com.abhay.restaurantapp.domain.FoodRepository
import com.abhay.restaurantapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        val cartItems = _cartState.value.items
        var netTotal = 0.0
        cartItems.forEach { (menuItem, _, quantity) ->
            netTotal += menuItem.price.toDouble() * quantity
        }
        val cgst = netTotal * 0.025
        val sgst = netTotal * 0.025
        val grandTotal = netTotal + cgst + sgst
        val roundedGrandTotal = String.format("%.2f", grandTotal).toDouble()

        _cartState.value = _cartState.value.copy(
            netTotalAmount = netTotal, grandTotalAmount = roundedGrandTotal
        )
    }

    fun checkout(openDialog: (String, String) -> Unit) {
        _cartState.value = _cartState.value.copy(isPaymentLoading = true)
        viewModelScope.launch {
            val paymentItems = _cartState.value.items.map { cartItem ->
                PaymentItem(
                    cuisineId = cartItem.cuisineId.toInt(),
                    itemId = cartItem.item.id.toInt(),
                    itemPrice = cartItem.item.price.toDouble(),
                    itemQuantity = cartItem.quantity
                )
            }
            Log.d("CheckoutViewModel", "checkout: $paymentItems")
            val resource = foodRepository.makePayment(
                totalAmount = _cartState.value.grandTotalAmount.toString(),
                totalItems = _cartState.value.items.size,
                paymentItems = paymentItems
            )
            when (resource) {
                is Resource.Error<*> -> {
                    _cartState.value = _cartState.value.copy(
                        error = resource.message, isPaymentLoading = false
                    )
                }

                is Resource.Success<*> -> {
                    Log.d("CheckoutViewModel", "checkout: ${resource.data!!.responseMessage}")
                    _cartState.value = _cartState.value.copy(isPaymentLoading = false)
                    openDialog(
                        resource.data!!.transactionReferenceNumber, resource.data.responseMessage
                    )
                }
            }
        }
    }

    fun clearError() {
        _cartState.update { it.copy(error = null) }
    }

}


data class CartState(
    val netTotalAmount: Double = 0.0,
    val grandTotalAmount: Double = 0.0,
    val items: List<CartItem> = emptyList(),
    val error: String? = null,
    val isPaymentLoading: Boolean = false
)