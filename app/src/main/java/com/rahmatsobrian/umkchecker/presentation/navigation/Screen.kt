package com.rahmatsobrian.umkchecker.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorite : Screen("favorite")
    data object Detail : Screen("detail/{umkId}") {
        fun createRoute(umkId: Long) = "detail/$umkId"
    }
}
