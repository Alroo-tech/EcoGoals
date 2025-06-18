package com.example.tusecogoals.entities.firestore

/**
 * File: Tip.kt
 * Description: This file contains the Challenge data class for managing challenges from Firebase
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
data class Tip(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val createdAt: String = ""

)
