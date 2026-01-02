package com.example.basicapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicapp.BuildConfig
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.data.User.toEntity
import com.example.basicapp.data.weather.Weather
import com.example.basicapp.db.userdetail.UserDetailEntity
import com.example.basicapp.db.userdetail.UserDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailScreenViewModel(val userDetailRepository: UserDetailRepository): ViewModel() {
    private val TAG: String = "DetailScreenViewModel"
    private val _userUiState = MutableStateFlow<NetworkResponse<UserDetailEntity>>(NetworkResponse.Loading)
    val userUiState: MutableStateFlow<NetworkResponse<UserDetailEntity>> = _userUiState

    private val _weatherUiState = MutableStateFlow<NetworkResponse<Weather>>(NetworkResponse.Loading)
    val weatherUiState: MutableStateFlow<NetworkResponse<Weather>> = _weatherUiState
    private val weatherService = RetroFitInstance.weatherServiceGetInstance

    fun getUserDetail(id: Int){
        viewModelScope.launch {
            try{
                val result = userDetailRepository.getUser(id)
                _userUiState.value = NetworkResponse.Success(result)
            } catch (e: Exception){
                Log.d(TAG, "error occurred "+e.message.toString())
                NetworkResponse.Error(e.message.toString())
            }
        }
    }
    fun getWeatherDetail(lat: Double, lon: Double){
        viewModelScope.launch {
            try{
                val response = weatherService.getWeatherDetail(lat, lon)
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data!=null) {
                        weatherUiState.value = NetworkResponse.Success(data)
                    } else {
                        Log.d(TAG, "unsuccessful request "+"body is null")
                    }
                } else {
                    weatherUiState.value = NetworkResponse.Error("fasdsa")
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "error occurred "+e.message.toString())
                NetworkResponse.Error(e.message.toString())
            }
        }
    }
}