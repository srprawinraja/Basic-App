package com.example.basicapp.db.userdetail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserDetailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // auto-increment
    @ColumnInfo(name = "profile_pic") val profilePic: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "dob") val dob: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "ph") val ph: String,
)