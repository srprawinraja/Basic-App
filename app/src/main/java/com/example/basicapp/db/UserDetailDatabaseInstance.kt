package com.example.basicapp.db

import android.content.Context
import androidx.room.Room

class UserDetailDatabaseInstance {
    companion object {
        @Volatile
        private var INSTANCE: UserDetailDatabase? = null

        fun getInstance(context: Context): UserDetailDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                UserDetailDatabase::class.java,
                "app_db"
            ).build().also { INSTANCE = it }
        }
    }
}