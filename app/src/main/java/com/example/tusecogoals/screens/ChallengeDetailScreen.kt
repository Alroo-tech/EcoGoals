package com.example.tusecogoals.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tusecogoals.R
import com.example.tusecogoals.ui.theme.PrimaryColor
import com.example.tusecogoals.viewmodels.ChallengeViewModel
import com.example.tusecogoals.viewmodels.HomeViewModel

/**
 * File: ChallengeDetailScreen.kt
 * Description: This file contains the Challenge composable for displaying
 *              the current challenge and allowing the user to complete it.
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    challengeViewModel: ChallengeViewModel = viewModel(),
    userId: String,
) {
    val challenge by challengeViewModel.dailyChallenge.collectAsState()
    val errorMessage by challengeViewModel.errorMessage.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    // Handle navigation to home
    LaunchedEffect(Unit) {
        challengeViewModel.navigateToHome.collect {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    // Display Error Dialog
    if (errorMessage != null && errorMessage!!.isNotEmpty()) {
        showErrorDialog = true
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(text = "Error") },
            text = { Text(text = errorMessage ?: "An unknown error occurred.") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Challenge Details
                item {
                    if (challenge == null) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 16.dp)
                        )
                    } else {
                        ChallengeDetailCard(
                            title = challenge!!.title,
                            description = challenge!!.description,
                            difficulty = challenge!!.difficulty,
                            onComplete = {
                                challengeViewModel.completeChallengeAndFetchNew(
                                    userId = userId,
                                    currentChallenge = challenge!!
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ChallengeDetailTopBar(onMenuClick: () -> Unit, content: @Composable () -> Unit) {
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
fun ChallengeDetailCard(
    title: String,
    description: String,
    difficulty: Int,
    onComplete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Difficulty: $difficulty",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Complete Challenge", color = PrimaryColor)
            }
        }
    }
}
