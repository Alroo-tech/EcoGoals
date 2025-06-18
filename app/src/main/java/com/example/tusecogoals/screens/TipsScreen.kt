package com.example.tusecogoals.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.tusecogoals.entities.firestore.Tip
import com.example.tusecogoals.ui.theme.ElectricYellow
import com.example.tusecogoals.ui.theme.SkyBlue
import com.example.tusecogoals.ui.theme.SalmonPink
import com.example.tusecogoals.ui.theme.PrimaryColor
import com.example.tusecogoals.viewmodels.HomeViewModel
import com.example.tusecogoals.viewmodels.TipsViewModel

/**
 * File: TipsScreen.kt
 * Description: This file contains the TipsScreen composable for displaying tips from Firebase
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(viewModel: TipsViewModel = viewModel(), homeViewModel: HomeViewModel, navController: NavController) {
    val tips by viewModel.tips.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var expanded by remember { mutableStateOf(false) } // State for dropdown menu
    var showMenu by remember { mutableStateOf(false) }

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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Loading State
                if (isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 16.dp),
                            color = PrimaryColor
                        )
                    }
                } else if (tips.isEmpty()) {
                    // Empty State
                    item {
                        Text(
                            text = "No tips available.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                    }
                } else {
                    // Tips List with Alternating Card Colors
                    itemsIndexed(tips) { index, tip ->
                        val cardColor = when (index % 3) {
                            0 -> ElectricYellow
                            1 -> SkyBlue
                            else -> SalmonPink
                        }
                        TipItem(tip = tip, cardColor = cardColor)
                    }
                }
            }
        }
    )
}

@Composable
fun TipsTopBar(onMenuClick: () -> Unit, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TUS Logo
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

        // Dropdown Content
        content()
    }
}

@Composable
fun TipItem(tip: Tip, cardColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tip.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tip.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Category: ${tip.category}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}
