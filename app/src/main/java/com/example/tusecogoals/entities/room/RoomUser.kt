// app/src/main/java/com/example/tusecogoals/entities/room/RoomUser.kt
package com.example.tusecogoals.entities.room

import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * File: RoomUser.kt
 * Description: This file contains the RoomUser data class for managing users from Room
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Entity(tableName = "users")
data class RoomUser(
    @PrimaryKey val id: String = "",
    val username: String = "",
    val email: String = "",
    val createdAt: Long? = null,
    val points: Int = 0
)
