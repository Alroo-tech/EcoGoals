package com.example.tusecogoals.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tusecogoals.R
import com.example.tusecogoals.ui.theme.SeaFoamGreen
import com.example.tusecogoals.ui.theme.SkyBlue

/**
 * File: BottomNavigationBar.kt
 * Description: This file contains the BottomNavigationBar composable for the bottom navigation bar
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.homeicon, R.drawable.homeiconclicked),
        BottomNavItem("events", "Events", R.drawable.eventsicon, R.drawable.eventsiconclicked),
        BottomNavItem("challenge", "Challenge", R.drawable.newsicon, R.drawable.newsiconclicked),
        BottomNavItem("tips", "Tips", R.drawable.tipsicon, R.drawable.tipsiconclicked),
        BottomNavItem("map/52.6750518/-8.6510599", "Map", R.drawable.mapicon, R.drawable.mapiconclicked)
    )

    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        val currentRoute by navController.currentBackStackEntryAsState()

        items.forEach { item ->
            val isSelected = currentRoute?.destination?.route == item.route ||
                    currentRoute?.destination?.route?.startsWith(item.route.split("/")[0]) == true

            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.label,
                        modifier = Modifier.size(46.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) SeaFoamGreen else SkyBlue,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                selected = isSelected,
                onClick = {
                    val destinationRoute = if (item.route.startsWith("map")) {
                        "map/52.6750518/-8.6510599" // Default Map coordinates for TUS Limerick
                    } else {
                        item.route
                    }
                    if (currentRoute?.destination?.route != destinationRoute) {
                        navController.navigate(destinationRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Unspecified,
                    unselectedIconColor = Color.Unspecified,
                    selectedTextColor = SeaFoamGreen,
                    unselectedTextColor = SkyBlue,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
)
