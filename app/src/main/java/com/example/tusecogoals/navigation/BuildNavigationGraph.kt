// File: BuildNavigationGraph.kt
package com.example.tusecogoals.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tusecogoals.screens.*
import com.example.tusecogoals.viewmodels.AuthViewModel
import com.example.tusecogoals.viewmodels.ChallengeViewModel
import com.example.tusecogoals.viewmodels.HomeViewModel

/**
 * File: BuildNavigationGraph.kt
 * Description: This file contains the BuildNavigationGraph composable for navigation around the app
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

@Composable
fun BuildNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    challengeViewModel: ChallengeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "home" else "login",
        modifier = modifier
    ) {
        // Public Screens
        composable("login") {
            LoginScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable("signup") {
            SignUpScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable("contact") {
            ContactScreen(navController = navController)
        }

        // Protected Screens
        composable("home") {
            AuthGuard(navController, isAuthenticated) {
                HomeScreen(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    challengeViewModel = challengeViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable("events") {
            AuthGuard(navController, isAuthenticated) {
                EventsScreen(
                    navController = navController,
                    homeViewModel = homeViewModel,)
            }
        }

        composable("challenge") {
            AuthGuard(navController, isAuthenticated) {
                ChallengeDetailScreen(
                    navController = navController,
                    challengeViewModel = challengeViewModel,
                    homeViewModel = homeViewModel,
                    userId = homeViewModel.currentUserId // Ensure ViewModel exposes currentUserId
                )
            }
        }

        composable("tips") {
            AuthGuard(navController, isAuthenticated) {
                TipsScreen(homeViewModel = homeViewModel, navController = navController)
            }
        }

        composable(
            "map/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble() ?: 52.6750518
            val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble() ?: -8.6510599
            AuthGuard(navController, isAuthenticated) {
                MapScreen(
                    navController = navController,
                    latitude = latitude,
                    longitude = longitude
                )
            }
        }
    }
}

@Composable
fun AuthGuard(
    navController: NavHostController,
    isAuthenticated: Boolean,
    content: @Composable () -> Unit
) {
    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            Log.w("AuthGuard", "User not authenticated. Redirecting to LoginScreen.")
            navController.navigate("login") {
                popUpTo("home") { inclusive = true } // Adjusted to pop up to "home"
                launchSingleTop = true
                restoreState = true
            }
        } else {
            Log.d("AuthGuard", "User authenticated. Displaying protected content.")
        }
    }

    // If user is authenticated, render the protected content
    if (isAuthenticated) {
        content()
    }
}
