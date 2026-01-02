package com.example.basicapp.db.userdetail

import android.content.Context
import com.example.basicapp.db.UserDetailDatabaseInstance

class UserDetailRepository(context: Context) {
    val db = UserDetailDatabaseInstance.Companion.getInstance(context)
    val userDetailDao = db.userDetailDao()
    suspend fun insertAll(users: List<UserDetailEntity>){
       userDetailDao.insertAll(users)
    }
    suspend fun getAllUsersDetail() = userDetailDao.getAllUser()


    suspend fun getUser(id: Int) = userDetailDao.getUser(id)

    suspend fun clearUsers() = userDetailDao.clearUsers()

    suspend fun getFilteredUsers(query: String) = userDetailDao.filterBasedOnName(query)

}