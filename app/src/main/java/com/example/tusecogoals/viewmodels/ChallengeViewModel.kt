package com.example.tusecogoals.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusecogoals.entities.firestore.Challenge
import com.example.tusecogoals.entities.firestore.UserChallenge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
/**
 * File: ChallengeViewModel.kt
 * Description: This file contains the ChallengeViewModel class for managing challenges
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    // Firebase Components
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // StateFlows to manage challenge data and errors
    private val _dailyChallenge = MutableStateFlow<Challenge?>(null)
    val dailyChallenge: StateFlow<Challenge?> get() = _dailyChallenge

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // SharedFlow for navigation events (e.g., navigate to Home)
    private val _navigateToHome = MutableSharedFlow<Unit>()
    val navigateToHome: SharedFlow<Unit> get() = _navigateToHome

    init {
        assignDailyChallenge()
    }

    // Helper function to get the current date in "yyyy-MM-dd" format
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Assigns the daily challenge by checking if one is already assigned for today
    private fun assignDailyChallenge() {
        val user = auth.currentUser
        if (user == null) {
            _errorMessage.value = "User not authenticated."
            return
        }

        val userId = user.uid
        val today = getCurrentDate()

        viewModelScope.launch {
            try {
                val userChallengeRef = firestore
                    .collection("user_challenges")
                    .document(userId)
                    .collection("daily_challenges")
                    .document(today)

                val document = userChallengeRef.get().await()

                if (document.exists()) {
                    val userChallenge = document.toObject(UserChallenge::class.java)
                    userChallenge?.let { fetchChallengeDetails(it.challengeId) }
                } else {
                    selectAndAssignChallenge(userId, today)
                }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "Error assigning daily challenge: ${e.message}")
                _errorMessage.value = "Failed to assign today's challenge."
            }
        }
    }

    // Fetches challenge details based on the challenge ID
    private suspend fun fetchChallengeDetails(challengeId: String) {
        try {
            val document = firestore.collection("challenges").document(challengeId).get().await()
            if (document.exists()) {
                val challenge = document.toObject(Challenge::class.java)?.apply {
                    id = document.id
                }
                _dailyChallenge.value = challenge
            } else {
                _errorMessage.value = "Challenge details not found."
            }
        } catch (e: Exception) {
            Log.e("ChallengeViewModel", "Error fetching challenge details: ${e.message}")
            _errorMessage.value = "Failed to fetch challenge details."
        }
    }

    // Selects and assigns a new challenge to the user for today
    private suspend fun selectAndAssignChallenge(userId: String, date: String) {
        try {
            val snapshot = firestore.collection("challenges").get().await()
            val challenges = snapshot.documents.mapNotNull { document ->
                document.toObject(Challenge::class.java)?.apply {
                    id = document.id
                }
            }

            if (challenges.isEmpty()) {
                _errorMessage.value = "No challenges available."
                return
            }

            val selectedChallenge = challenges.random()

            val userChallenge = UserChallenge(
                challengeId = selectedChallenge.id,
                assignedAt = Date()
            )

            firestore.collection("user_challenges")
                .document(userId)
                .collection("daily_challenges")
                .document(date)
                .set(userChallenge).await()

            _dailyChallenge.value = selectedChallenge

        } catch (e: Exception) {
            Log.e("ChallengeViewModel", "Error assigning challenge: ${e.message}")
            _errorMessage.value = "Failed to assign today's challenge."
        }
    }

    // Completes the current challenge, adds points to the user, fetches a new challenge, and navigates to Home
    fun completeChallengeAndFetchNew(userId: String, currentChallenge: Challenge) {
        viewModelScope.launch {
            try {
                if (currentChallenge.id.isEmpty()) {
                    Log.e("ChallengeViewModel", "Invalid challenge ID")
                    _errorMessage.value = "Invalid challenge data. Please try again."
                    return@launch
                }

                // Mark challenge as completed in Firestore
                firestore.collection("challenges").document(currentChallenge.id)
                    .update("isCompleted", true).await()

                // Add points to the user's total using a Firestore transaction for atomicity
                val userRef = firestore.collection("users").document(userId)
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(userRef)
                    val currentPoints = snapshot.getLong("points") ?: 0
                    transaction.update(
                        userRef,
                        "points",
                        currentPoints + currentChallenge.difficulty
                    )
                }.await()

                // Fetch a new challenge
                val snapshot = firestore.collection("challenges")
                    .whereEqualTo("isCompleted", false)
                    .get()
                    .await()

                val newChallenges = snapshot.documents.mapNotNull {
                    it.toObject(Challenge::class.java)?.apply { id = it.id }
                }
                val newChallenge = newChallenges.randomOrNull()

                if (newChallenge != null) {
                    _dailyChallenge.value = newChallenge
                } else {
                    _errorMessage.value = "No new challenges available."
                }

                // Emit navigation event to navigate back to Home
                _navigateToHome.emit(Unit)

            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "Error completing challenge: ${e.message}")
                _errorMessage.value = "Failed to complete challenge. Please try again."
            }
        }
    }

}
