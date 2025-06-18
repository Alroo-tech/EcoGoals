// app/src/main/java/com/example/tusecogoals/data/room/UserDao.kt
package com.example.tusecogoals.database

import androidx.room.*
import com.example.tusecogoals.entities.room.RoomUser
import kotlinx.coroutines.flow.Flow
/**
 * File: UserDao.kt
 * Description: This file contains the UserDao interface for managing user data in the Room database
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: RoomUser)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<RoomUser?>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<RoomUser>>

    @Update
    suspend fun updateUser(user: RoomUser)

    @Delete
    suspend fun deleteUser(user: RoomUser)

    @Query("UPDATE users SET points = points + :points WHERE id = :userId")
    suspend fun addPointsToUser(userId: String, points: Int)


    @Query("SELECT * FROM users ORDER BY points DESC")
    fun getAllUsersSortedByPoints(): Flow<List<RoomUser>>

}
