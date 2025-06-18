package com.example.tusecogoals.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusecogoals.database.TipsRepository
import com.example.tusecogoals.entities.firestore.Tip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * File: TipsViewModel.kt
 * Description: This file contains the TipsViewModel class for managing tips
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

class TipsViewModel : ViewModel() {
    private val repository = TipsRepository()

    private val _tips = MutableStateFlow<List<Tip>>(emptyList())
    val tips: StateFlow<List<Tip>> get() = _tips

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchTips()
    }

    private fun fetchTips() {
        viewModelScope.launch {
            _isLoading.value = true
            _tips.value = repository.getAllTips()
            _isLoading.value = false
        }
    }
}
