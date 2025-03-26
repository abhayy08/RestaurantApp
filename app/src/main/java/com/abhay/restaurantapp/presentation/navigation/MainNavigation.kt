package com.abhay.restaurantapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.abhay.restaurantapp.presentation.cart.CheckoutScreen
import com.abhay.restaurantapp.presentation.cart.CheckoutViewModel
import com.abhay.restaurantapp.presentation.cuisine.CuisineScreen
import com.abhay.restaurantapp.presentation.cuisine.CuisineViewModel
import com.abhay.restaurantapp.presentation.home.HomeScreen
import com.abhay.restaurantapp.presentation.home.HomeViewModel
import com.abhay.restaurantapp.presentation.menu.MenuScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(
    navController: NavHostController,
    paddValues: PaddingValues
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val cartItems = homeViewModel.uiState.collectAsState().value.cart
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddValues)
    ) {
        composable<Home> {
            val state = homeViewModel.uiState.collectAsState().value
            HomeScreen(
                uiState = state,
                onCuisineClick = {
                    navController.navigate(
                        Cuisine(
                            id = it
                        )
                    )
                },
                onAddItem = {
                    homeViewModel.addItemToCart(it, state.cuisineIdofTopItems)
                },
                onRemoveItem = {
                    homeViewModel.removeItemFromCart(it)
                }
            )
        }

        composable<Cuisine> {
            val args = it.toRoute<Cuisine>()
            val viewModel = hiltViewModel<CuisineViewModel>()
            viewModel.initializeViewModel(args.id, cartItems)
            val state = viewModel.uiState.collectAsState().value

            CuisineScreen(
                state = state,
                onAddItem = {
                    homeViewModel.addItemToCart(it, args.id)
                },
                onRemoveItem = {
                    homeViewModel.removeItemFromCart(it)
                },
                onPopBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Menu> {
            MenuScreen(
                cart = cartItems,
                onAddItem = {
                    homeViewModel.addItemToCart(it.item, it.cuisineId)
                },
                onRemoveItem = {
                    homeViewModel.removeItemFromCart(it.item)
                },
                onPopBack = {
                    navController.popBackStack()
                },
                onProceedToCheckout = {
                    navController.navigate(CheckOut)
                }
            )
        }

        composable<CheckOut> {
            val viewModel: CheckoutViewModel = hiltViewModel()
            viewModel.initializeCart(cartItems)
            val cartState = viewModel.cartState.collectAsState().value

            CheckoutScreen(
                cartState = cartState,
                onCheckOut = {

                }
            )
        }
    }
}

@Serializable
data object Home

@Serializable
data class Cuisine(val id: String)

@Serializable
data object Menu

@Serializable
data object CheckOut