// File: AuthViewModel.kt
package com.example.tusecogoals.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * File: AuthViewModel.kt
 * Description: This file contains the AuthViewModel class for handling authentication
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> get() = _isAuthenticated

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isAuthenticated.value = firebaseAuth.currentUser != null
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUsername(): String? {
        return auth.currentUser?.email // Modify as per your user data structure
    }
}
