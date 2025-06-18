package com.example.tusecogoals.entities.firestore

/**
 * File: Event.kt
 * Description: This file contains the Challenge data class for managing challenges from Firebase
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val category: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
