package com.rahmatsobrian.umkchecker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rahmatsobrian.umkchecker.presentation.detail.DetailScreen
import com.rahmatsobrian.umkchecker.presentation.favorite.FavoriteScreen
import com.rahmatsobrian.umkchecker.presentation.home.HomeScreen

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Beranda", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Screen.Favorite, "Favorit", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmkNavGraph() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    val showBottomBar = bottomNavItems.any { it.screen.route == currentRoute?.route }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute?.hierarchy?.any { it.route == item.screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onUmkClick = { umk -> navController.navigate(Screen.Detail.createRoute(umk.id)) },
                    snackbarHostState = snackbarHostState
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    onUmkClick = { umk -> navController.navigate(Screen.Detail.createRoute(umk.id)) }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("umkId") { type = androidx.navigation.NavType.LongType })
            ) {
                DetailScreen(
                    onBack = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
