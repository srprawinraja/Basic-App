package com.example.basicapp.db.userdetail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailDao {

    
    @Insert
    suspend fun insertAll(userDetailEntities: List<UserDetailEntity>)


    @Query("SELECT * FROM UserDetailEntity WHERE id=:id")
    suspend fun getUser(id: Int): UserDetailEntity

    @Query("SELECT * FROM UserDetailEntity")
    suspend fun getAllUser(): List<UserDetailEntity>

    @Query("DELETE FROM UserDetailEntity")
    suspend fun clearUsers()

    @Query("SELECT * FROM UserDetailEntity " +
            "where first_name LIKE '%' || :query || '%' " +
            "OR  last_name LIKE '%' || :query || '%' " +
            "OR (first_name || ' ' || last_name) LIKE '%' || :query || '%'")
    suspend fun filterBasedOnName(query: String): List<UserDetailEntity>
}