package com.example.basicapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.basicapp.db.userdetail.UserDetailRepository
import com.example.basicapp.viewmodels.DetailScreenViewModel
import com.example.basicapp.viewmodels.ListScreenViewModel

class UserServiceFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val userDetailRepository = UserDetailRepository(context)
        if (modelClass.isAssignableFrom(ListScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListScreenViewModel(
                userDetailRepository
            ) as T
        } else if (modelClass.isAssignableFrom(DetailScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailScreenViewModel(
                userDetailRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}