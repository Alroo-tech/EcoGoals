// app/src/main/java/com/example/tusecogoals/entities/firestore/UserChallenge.kt
package com.example.tusecogoals.entities.firestore
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
/**
 * File: UserChallenge.kt
 * Description: This file contains the UserChallenge data class for managing user challenges from Firebase
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@IgnoreExtraProperties
data class UserChallenge(
    @get:PropertyName("challengeId") @set:PropertyName("challengeId") var challengeId: String = "",
    @get:PropertyName("assignedAt") @set:PropertyName("assignedAt") @ServerTimestamp var assignedAt: Date? = null,
    @get:PropertyName("isCompleted") @set:PropertyName("isCompleted") var isCompleted: Boolean = false
)
