package com.abhay.restaurantapp

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.abhay.restaurantapp.presentation.RestaurantMainScreen
import com.abhay.restaurantapp.presentation.home.HomeViewModel
import com.abhay.restaurantapp.ui.theme.RestaurantAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val homeViewModel = hiltViewModel<HomeViewModel>()

            RestaurantAppTheme {
                RestaurantMainScreen(
                    navController,
                    homeViewModel,
                    onLanguageChange = {
                        homeViewModel.toggleLanguage()
                        val newLanguage = homeViewModel.language.value
                        setLocale(this, newLanguage)
                    }
                )

            }
        }
    }
}

fun setLocale(activity: Activity, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(activity.resources.configuration)
    config.setLocale(locale)

    val context = activity.createConfigurationContext(config)
    activity.resources.updateConfiguration(config, context.resources.displayMetrics)

    activity.recreate()
}




