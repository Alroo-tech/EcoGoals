// app/src/main/java/com/example/tusecogoals/data/room/UserRepository.kt
package com.example.tusecogoals.database

import com.example.tusecogoals.entities.room.RoomUser
import kotlinx.coroutines.flow.Flow
/**
 * File: UserRepository.kt
 * Description: This file contains the UserRepository class for managing user data in the Room database
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
class UserRepository(private val userDao: UserDao) {


    suspend fun insertUser(user: RoomUser) {
        userDao.insertUser(user)
    }


    fun getUserById(id: String): Flow<RoomUser?> {
        return userDao.getUserById(id)
    }


    fun getAllUsers(): Flow<List<RoomUser>> {
        return userDao.getAllUsers()
    }

    suspend fun updateUser(user: RoomUser) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: RoomUser) {
        userDao.deleteUser(user)
    }

    fun getLeaderboard(): Flow<List<RoomUser>> {
        return userDao.getAllUsersSortedByPoints()
    }
}
