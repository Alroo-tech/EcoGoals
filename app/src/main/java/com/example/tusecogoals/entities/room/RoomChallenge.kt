package com.example.tusecogoals.entities.room
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * File: RoomChallenge.kt
 * Description: This file contains the RoomChallenge data class for managing challenges from Room
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Entity(tableName = "challenges")
data class RoomChallenge(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val description: String = "",
    val difficulty: Int = 1, // Int type
    val userId: String = "",
    val createdAt: Long = 0L,
    val isCompleted: Boolean = false,
    val isAccepted: Boolean = false
)
