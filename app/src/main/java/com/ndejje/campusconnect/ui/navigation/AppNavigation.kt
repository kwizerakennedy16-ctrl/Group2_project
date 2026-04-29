package com.ndejje.campusconnect.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Map : Screen("map")
    object Dashboard : Screen("dashboard")
    object Resources : Screen("resources")
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val screen: Screen
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Filled.Home,
        label = "Home",
        screen = Screen.Home
    ),
    BottomNavItem(
        route = Screen.Map.route,
        icon = Icons.Filled.Map,
        label = "Map",
        screen = Screen.Map
    ),
    BottomNavItem(
        route = Screen.Dashboard.route,
        icon = Icons.Filled.MenuBook,
        label = "Dashboard",
        screen = Screen.Dashboard
    ),
    BottomNavItem(
        route = Screen.Resources.route,
        icon = Icons.Filled.Person,
        label = "Resources",
        screen = Screen.Resources
    )
)
