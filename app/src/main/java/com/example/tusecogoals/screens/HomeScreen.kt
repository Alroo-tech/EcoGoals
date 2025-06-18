package com.example.tusecogoals.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tusecogoals.R
import com.example.tusecogoals.entities.firestore.User
import com.example.tusecogoals.ui.theme.PrimaryColor
import com.example.tusecogoals.ui.theme.SalmonPink
import com.example.tusecogoals.ui.theme.SeaFoamGreen
import com.example.tusecogoals.ui.theme.SkyBlue
import com.example.tusecogoals.viewmodels.AuthViewModel
import com.example.tusecogoals.viewmodels.ChallengeViewModel
import com.example.tusecogoals.viewmodels.HomeViewModel

/**
 * File: HomeScreen.kt
 * Description: This file contains the HomeScreen composable for displaying
 *              user-specific information, daily challenges, and the leaderboard.
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    challengeViewModel: ChallengeViewModel,
    authViewModel: AuthViewModel
) {
    val dailyChallenge by challengeViewModel.dailyChallenge.collectAsState()
    val leaderboard by homeViewModel.leaderboard.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    val username by homeViewModel.username.collectAsState()

    // fixes error when authentication state changes to refresh data
    LaunchedEffect(authViewModel.isAuthenticated.collectAsState().value) {
        if (authViewModel.isAuthenticated.value) {
            homeViewModel.fetchUsername()
            homeViewModel.listenToLeaderboard()
        }
    }

    // Main Content
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(showMenu = showMenu, onMenuClick = { showMenu = !showMenu }) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Contact") },
                        onClick = {
                            navController.navigate("contact")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            homeViewModel.signOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Hello, $username!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = SeaFoamGreen,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Start
                    )
                }
                // Daily Challenge Section
                item {
                    dailyChallenge?.let { challenge ->
                        ChallengeCard(
                            title = challenge.title,
                            description = challenge.description,
                            difficulty = challenge.difficulty,
                            onClick = { navController.navigate("challenge") }
                        )
                    } ?: CircularProgressIndicator()
                }

                // Leaderboard Section
                item {
                    LeaderboardCard(leaderboard = leaderboard)
                }
            }
        }
    )
}

@Composable
fun TopAppBar(showMenu: Boolean, onMenuClick: () -> Unit, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.tuslogo),
            contentDescription = "TUS Logo",
            modifier = Modifier.size(130.dp),
            contentScale = ContentScale.Fit
        )
        IconButton(onClick = onMenuClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = PrimaryColor,
                modifier = Modifier.size(42.dp)
            )
        }
        content()
    }
}

@Composable
fun ChallengeCard(title: String, description: String, difficulty: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SalmonPink),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Difficulty: $difficulty", style = MaterialTheme.typography.bodySmall, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)) {
                Text("View Challenge Details", color = Color.White)
            }
        }
    }
}

@Composable
fun LeaderboardCard(leaderboard: List<User>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = SkyBlue), // Transparent background
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sustainability Leaders",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (leaderboard.isEmpty()) {
                Text(
                    text = "No users found.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(leaderboard) { user ->
                        LeaderboardRow(user)
                        Divider(color = Color.LightGray, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = user.username.ifEmpty { "Unknown User" },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Text(
            text = "${user.points} points",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}
