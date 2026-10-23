package com.example.inventiapp.navigation

sealed class Screen(val route: String) {

    data object Login : Screen(route = "login_page")
    data object Home : Screen(route = "home_page")
    data object Movements : Screen(route = "movements_page")
    data object Settings : Screen(route = "settings_page")
}