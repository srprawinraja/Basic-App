package com.example.basicapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basicapp.db.userdetail.UserDetailDao
import com.example.basicapp.db.userdetail.UserDetailEntity


@Database(entities = [UserDetailEntity::class], version = 1, exportSchema = true)
abstract class UserDetailDatabase : RoomDatabase() {
    abstract fun userDetailDao(): UserDetailDao
}