// app/src/main/java/com/example/tusecogoals/entities/firestore/Challenge.kt
package com.example.tusecogoals.entities.firestore

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
/**
 * File: Challenge.kt
 * Description: This file contains the Challenge data class for managing challenges from Firebase
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@IgnoreExtraProperties
data class Challenge(
    @DocumentId var id: String = "", // Firestore document ID
    @get:PropertyName("challengeId") @set:PropertyName("challengeId") var challengeId: String = "",
    @get:PropertyName("title") @set:PropertyName("title") var title: String = "",
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("difficulty") @set:PropertyName("difficulty") var difficulty: Int = 1,
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") @ServerTimestamp var createdAt: Date? = null,
    @get:PropertyName("isCompleted") @set:PropertyName("isCompleted") var isCompleted: Boolean = false,
    @get:PropertyName("latitude") @set:PropertyName("latitude") var latitude: Double = 0.0, // Added latitude
    @get:PropertyName("longitude") @set:PropertyName("longitude") var longitude: Double = 0.0, // Added longitude
    @get:PropertyName("isAccepted") @set:PropertyName("isAccepted") var isAccepted: Boolean = false
)
