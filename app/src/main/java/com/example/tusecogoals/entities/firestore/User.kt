// app/src/main/java/com/example/tusecogoals/entities/firestore/User.kt
package com.example.tusecogoals.entities.firestore

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.Date
/**
 * File: User.kt
 * Description: This file contains the User data class for managing users from Firebase
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@IgnoreExtraProperties
data class User(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("username") @set:PropertyName("username") var username: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") var createdAt: Date? = null,
    @get:PropertyName("points") @set:PropertyName("points") var points: Int = 0
)
