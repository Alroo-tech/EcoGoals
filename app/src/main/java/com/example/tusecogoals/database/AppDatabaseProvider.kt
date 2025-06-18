// app/src/main/java/com/example/tusecogoals/data/room/AppDatabaseProvider.kt
package com.example.tusecogoals.database

import android.content.Context
import androidx.room.Room
import com.example.tusecogoals.database.AppDatabase

/**
 * File: AppDatabaseProvider.kt
 * Description: This file contains the AppDatabaseProvider object for providing the Room database
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
object AppDatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tusecogoals_database"
            )
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = instance
            instance
        }
    }
}
