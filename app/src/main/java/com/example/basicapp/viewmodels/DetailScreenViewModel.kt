package com.example.basicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.db.userdetail.UserDetailEntity
import com.example.basicapp.db.userdetail.UserDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(val userDetailRepository: UserDetailRepository): ViewModel() {
    private val TAG: String = "DetailScreenViewModel"
    private val _uiState = MutableStateFlow<NetworkResponse<UserDetailEntity>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<UserDetailEntity>> = _uiState
    private val weatherService = RetroFitInstance.weatherServiceGetInstance

    fun getUserDetail(id: Int){
        viewModelScope.launch {
            try{
                val result = userDetailRepository.getUser(id)
                _uiState.value = NetworkResponse.Success(result)
            } catch (e: Exception){
                Log.d(TAG, "error occurred "+e.message.toString())
                NetworkResponse.Error(e.message.toString())
            }
        }
    }
}