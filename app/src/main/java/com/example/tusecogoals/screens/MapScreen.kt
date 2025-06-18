package com.example.tusecogoals.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.tusecogoals.ui.theme.PrimaryColor
import com.google.android.gms.maps.model.CameraPosition
/**
 * File: MapScreen.kt
 * Description: This file contains the MapScreen composable for
 *              displaying location details of events.
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController, // Navigation Controller for back button
    latitude: Double,
    longitude: Double
) {
    val location = LatLng(latitude, longitude)
    var mapLoaded by remember { mutableStateOf(false) } // Map load state

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Event Location") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor)
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(location, 15f)
                    },
                    onMapLoaded = { mapLoaded = true }
                ) {
                    // Marker at the event location
                    Marker(
                        state = MarkerState(position = location),
                        title = "Event Location",
                        snippet = "Latitude: $latitude, Longitude: $longitude"
                    )
                }

                // Display a loading indicator while the map is loading
                if (!mapLoaded) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    )
}
