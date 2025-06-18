package com.example.tusecogoals.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
/**
 * File: HomeViewModelFactory.kt
 * Description: This file contains the HomeViewModelFactory class for creating instances of HomeViewModel
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
class HomeViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
