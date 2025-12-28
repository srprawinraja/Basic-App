package com.example.basicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicapp.api.RetroFitInstance
import kotlinx.coroutines.launch

class ListScreenViewModel: ViewModel() {
    private val TAG = "ListScreenViewModel"
    private val userDetailService = RetroFitInstance.getInstance
    fun getAllUserDetails(results: Int){
        viewModelScope.launch {
            try {
                val response = userDetailService.getAllUserDetails(results)
                if (response.isSuccessful) {
                    Log.d(TAG, response.body().toString())
                } else {
                    Log.d(TAG, "unsuccessful request "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "exception occurred ${e.message}")
            }
        }
    }
}