package com.example.tusecogoals.database

import com.example.tusecogoals.entities.firestore.Tip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
/**
 * File: TipsRepository.kt
 * Description: This file contains the TipsRepository class for managing tip data
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
class TipsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val tipsCollection = firestore.collection("tips")

    // Fetch all tips from Firestore
    suspend fun getAllTips(): List<Tip> {
        return try {
            val snapshot = tipsCollection.get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Tip::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
