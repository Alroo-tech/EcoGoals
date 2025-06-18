package com.example.tusecogoals

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tusecogoals.navigation.BottomNavigationBar
import com.example.tusecogoals.navigation.BuildNavigationGraph
import com.example.tusecogoals.ui.theme.GradientBackground
import com.example.tusecogoals.ui.theme.TusEcoGoalsTheme
import com.example.tusecogoals.viewmodels.ChallengeViewModel
import com.example.tusecogoals.viewmodels.HomeViewModel

/**
 * This file contains the method launching the MainApp
 *
 * @author Alan Rooney
 * @date 07/12/2024
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TusEcoGoalsTheme {
                MainApp()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        "default_channel",
                        "Default Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    val manager = getSystemService(NotificationManager::class.java)
                    manager.createNotificationChannel(channel)
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TusEcoGoalsTheme {
        Greeting("Android")
    }
}

@Composable
fun MainApp(
    homeViewModel: HomeViewModel = viewModel(),
    challengeViewModel: ChallengeViewModel = viewModel() // Correctly obtain ChallengeViewModel instance
) {
    val navController = rememberNavController() // Initialize NavController

    GradientBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(navController = navController) // Attach BottomNavigationBar with NavController
            },
            containerColor = Color.Transparent // Ensure Scaffold has a transparent background
        ) { innerPadding ->
            BuildNavigationGraph(
                modifier = Modifier.padding(innerPadding), // Pass padding to content
                navController = navController, // Pass the NavController to the navigation graph
                homeViewModel = homeViewModel, // Pass HomeViewModel to navigation graph
                challengeViewModel = challengeViewModel // Pass ChallengeViewModel to navigation graph
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewMainApp() {
    TusEcoGoalsTheme {
        MainApp()
    }
}
