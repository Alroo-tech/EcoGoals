package com.example.tusecogoals.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusecogoals.database.AppDatabaseProvider
import com.example.tusecogoals.database.UserRepository
import com.example.tusecogoals.entities.firestore.User
import com.example.tusecogoals.entities.room.RoomUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
/**
 * File: HomeViewModel.kt
 * Description: This file contains the HomeViewModel class for managing user data
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Firebase Components
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Room Components
    private val database = AppDatabaseProvider.getDatabase(application.applicationContext)
    private val userRepository = UserRepository(database.userDao())

    // User State
    private val _user = MutableStateFlow<RoomUser?>(null)
    val user: StateFlow<RoomUser?> get() = _user

    // Leaderboard State
    private val _leaderboard = MutableStateFlow<List<User>>(emptyList())
    val leaderboard: StateFlow<List<User>> get() = _leaderboard

    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> get() = _username

    // Error Messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage
    val currentUserId: String
        get() = auth.currentUser?.uid ?: ""

    private var listenerRegistration: ListenerRegistration? = null

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUsername()
            listenToLeaderboard()
        }
        /*fetchUser()
        listenToLeaderboard()
        fetchUsername()*/
    }

    // Fetches the current user's data from Firestore and updates the local Room database
    fun fetchUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("HomeViewModel", "Error fetching user data: ", error)
                        _errorMessage.value = "Failed to fetch user data."
                        return@addSnapshotListener
                    }
                    snapshot?.toObject(RoomUser::class.java)?.let { user ->
                        _user.value = user
                        viewModelScope.launch { userRepository.insertUser(user) }
                    }
                }
        } else {
            _user.value = null
        }
    }

    fun fetchUsername() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _errorMessage.value = "User not authenticated."
                    Log.e("HomeViewModel", "User not authenticated.")
                    return@launch
                }

                val userDoc = firestore.collection("users").document(currentUser.uid).get().await()

                if (userDoc.exists()) {
                    val user = userDoc.toObject(User::class.java)
                    _username.value = user?.username ?: "User"
                    Log.d("HomeViewModel", "Fetched username: ${_username.value}")
                } else {
                    _username.value = "User"
                    Log.w("HomeViewModel", "User document does not exist.")
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching username: ${e.localizedMessage}")
                _errorMessage.value = "Failed to load username: ${e.localizedMessage}"
            }
        }
    }


    fun listenToLeaderboard() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _errorMessage.value = "User not authenticated."
                    Log.e("HomeViewModel", "User not authenticated.")
                    return@launch
                }

                Log.d("HomeViewModel", "Setting up real-time listener for leaderboard.")

                // Listen to real-time updates in the "users" collection
                listenerRegistration = firestore.collection("users")
                    .orderBy("points", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("HomeViewModel", "Error listening to leaderboard: ${error.localizedMessage}")
                            _errorMessage.value = "Failed to load leaderboard: ${error.localizedMessage}"
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            Log.d("HomeViewModel", "Received ${snapshot.size()} users for leaderboard.")

                            val users = snapshot.documents.mapNotNull { document ->
                                document.toObject(User::class.java)?.apply {
                                    Log.d("HomeViewModel", "User: $username, Points: $points")
                                }
                            }

                            _leaderboard.value = users

                            Log.d("HomeViewModel", "Leaderboard updated")
                        }
                    }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception in listenToLeaderboard: ${e.localizedMessage}")
                _errorMessage.value = "Failed to load leaderboard: ${e.localizedMessage}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }

    // Signs out the user and clears relevant states
    fun signOut() {
        auth.signOut()
        _user.value = null
        _leaderboard.value = emptyList()
    }
}
