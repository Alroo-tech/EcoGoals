package com.example.tusecogoals.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tusecogoals.database.EventsRepository
import com.example.tusecogoals.entities.firestore.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * File: EventsViewModel.kt
 * Description: This file contains the EventsViewModel class for managing events
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */

class EventsViewModel : ViewModel() {
    private val repository = EventsRepository()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _events.value = repository.getAllEvents()
            _isLoading.value = false
        }
    }
}
