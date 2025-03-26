package com.abhay.restaurantapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhay.restaurantapp.presentation.navigation.MainNavigation
import com.abhay.restaurantapp.presentation.navigation.Menu
import com.abhay.restaurantapp.ui.theme.RestaurantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            RestaurantAppTheme {
                RestaurantMainScreen(navController)
            }
        }
    }
}

@Composable
fun RestaurantMainScreen(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route
    val currentScreenTitle =
        currentDestination.toString().substringAfterLast('.').substringBeforeLast('/')


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RestaurantTopAppBar(currentScreenTitle = if(currentScreenTitle.startsWith("Dialog")) "Checkout" else currentScreenTitle)
        },
        floatingActionButton = {
            CartFab(
                navController = navController,
                isVisible = !(currentScreenTitle == "CheckOut" || currentScreenTitle == "Menu")
            )
        }
    ) { paddValues ->
        MainNavigation(navController, paddValues)
    }
}

@Composable
fun CartFab(
    navController: NavController,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(
                durationMillis = 500
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(
                durationMillis = 500
            ))
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Menu)
            }
        ) {
            Icon(Icons.Rounded.ShoppingCart, contentDescription = "Cart")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantTopAppBar(
    currentScreenTitle: String
) {
    TopAppBar(
        title = { Text(currentScreenTitle, style = MaterialTheme.typography.headlineLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}

