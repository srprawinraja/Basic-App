package com.example.basicapp.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.data.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel: ViewModel() {
    private val TAG = "ListScreenViewModel"
    private val userDetailService = RetroFitInstance.getInstance
    private val _uiState = MutableStateFlow<NetworkResponse<Users>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<Users>> = _uiState

    fun getAllUserDetails(results: Int){
        viewModelScope.launch {
            try {
                val response = userDetailService.getAllUserDetails(results)
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data!=null) {
                        _uiState.value = NetworkResponse.Success(data)
                    } else {
                        Log.d(TAG, "unsuccessful request "+"body is null")
                    }
                } else {
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "exception occurred ${e.message}")
            }
        }
    }

}