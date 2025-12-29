package com.example.basicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.data.Users
import com.example.basicapp.data.toEntity
import com.example.basicapp.db.userdetail.UserDetailEntity
import com.example.basicapp.db.userdetail.UserDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel(
    val userDetailRepository: UserDetailRepository
): ViewModel() {
    private val TAG = "ListScreenViewModel"
    private val userDetailService = RetroFitInstance.getInstance
    private val _uiState = MutableStateFlow<NetworkResponse<List<UserDetailEntity>>>(NetworkResponse.Loading)
    val uiState: MutableStateFlow<NetworkResponse<List<UserDetailEntity>>> = _uiState
    var pagination: Int = 0
    init {
        viewModelScope.launch {
            userDetailRepository.clearUsers()
        }
    }
    fun getAllUserDetails(){
        viewModelScope.launch {
            try {
                pagination+=25
                val response = userDetailService.getAllUserDetails(pagination)
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data!=null) {
                        userDetailRepository.insertAll(data.toEntity(data.results))
                        val dbData = userDetailRepository.getAllUsersDetail()
                        _uiState.value = NetworkResponse.Success(dbData)
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