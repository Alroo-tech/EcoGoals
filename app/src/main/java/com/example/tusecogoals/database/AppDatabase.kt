package com.example.tusecogoals.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tusecogoals.database.UserDao
import com.example.tusecogoals.entities.room.RoomChallenge
import com.example.tusecogoals.entities.room.RoomUser
/**
 * File: AppDatabase.kt
 * Description: This file contains the AppDatabase class for managing the Room database
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Database(
    entities = [RoomUser::class, RoomChallenge::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tus_eco_goals_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
