// app/src/main/java/com/example/tusecogoals/data/room/ChallengeDao.kt
package com.example.tusecogoals.database

import androidx.room.*
import com.example.tusecogoals.entities.room.RoomChallenge
import kotlinx.coroutines.flow.Flow
/**
 * File: ChallengeDao.kt
 * Description: This file contains the ChallengeDao interface for managing challenge data in the Room database
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Dao
interface ChallengeDao {

    /**
     * Retrieves all challenges for a specific user.
     */
    @Query("SELECT * FROM challenges WHERE userId = :userId")
    fun getAllChallenges(userId: String): Flow<List<RoomChallenge>>

    /**
     * Inserts a new challenge into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: RoomChallenge)

    /**
     * Deletes a challenge from the database.
     */
    @Delete
    suspend fun deleteChallenge(challenge: RoomChallenge)

    /**
     * Updates the completion status of a challenge.
     */
    @Query("UPDATE challenges SET isCompleted = :isCompleted WHERE id = :challengeId")
    suspend fun updateChallengeCompletion(challengeId: String, isCompleted: Boolean)

    /**
     * Updates the acceptance status of a challenge.
     */
    @Query("UPDATE challenges SET isAccepted = :isAccepted WHERE id = :challengeId")
    suspend fun updateChallengeAcceptance(challengeId: String, isAccepted: Boolean)

    /**
     * Retrieves a single challenge by its ID.
     */
    @Query("SELECT * FROM challenges WHERE id = :challengeId LIMIT 1")
    fun getChallengeById(challengeId: String): Flow<RoomChallenge?>
}
