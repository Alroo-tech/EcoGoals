package com.example.tusecogoals.database

import com.example.tusecogoals.entities.firestore.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
/**
 * File: EventsRepository.kt
 * Description: This file contains the EventsRepository class for managing event data
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
class EventsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")

    // Fetch all events from Firestore
    suspend fun getAllEvents(): List<Event> {
        return try {
            val snapshot = eventsCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList() // Return empty list if there's an error
        }
    }
}
