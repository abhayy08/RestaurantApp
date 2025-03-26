package com.abhay.restaurantapp.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.abhay.restaurantapp.presentation.cart.CheckoutScreen
import com.abhay.restaurantapp.presentation.cart.CheckoutViewModel
import com.abhay.restaurantapp.presentation.cuisine.CuisineScreen
import com.abhay.restaurantapp.presentation.cuisine.CuisineViewModel
import com.abhay.restaurantapp.presentation.dialog.DialogBox
import com.abhay.restaurantapp.presentation.home.HomeScreen
import com.abhay.restaurantapp.presentation.home.HomeViewModel
import com.abhay.restaurantapp.presentation.menu.MenuScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onShowSnackbar: (String) -> Unit = { message -> }
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val cartItems = homeViewModel.uiState.collectAsState().value.cart
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        }
    )
    {
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
                },
                onShowSnackbar = onShowSnackbar,
                clearError = { homeViewModel.clearError() }
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
                onShowSnackbar = onShowSnackbar,
                clearError = { viewModel.clearError() }
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
                    viewModel.checkout(
                        openDialog = { transactionId, message ->
                            navController.navigate(Dialog(transactionId, message))
                        }
                    )
                },
                onShowSnackbar = onShowSnackbar,
                clearError = { viewModel.clearError() }
            )
        }


        dialog<Dialog>(
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            val args = it.toRoute<Dialog>()
            DialogBox(
                transactionId = args.transactionId,
                message = args.message,
                popBackStack = {
                    navController.popBackStack(Home, false)
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
data class Dialog(val transactionId: String, val message: String)

@Serializable
data object CheckOut