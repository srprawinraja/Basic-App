package com.example.basicapp.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicapp.api.NetworkResponse
import com.example.basicapp.api.RetroFitInstance
import com.example.basicapp.data.User.toEntity
import com.example.basicapp.data.weather.Weather
import com.example.basicapp.db.userdetail.UserDetailEntity
import com.example.basicapp.db.userdetail.UserDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel(
    val userDetailRepository: UserDetailRepository
): ViewModel() {
    private val TAG = "ListScreenViewModel"
    private val userDetailService = RetroFitInstance.userServiceGetInstance
    private val _userUiState = MutableStateFlow<NetworkResponse<List<UserDetailEntity>>>(NetworkResponse.Loading)
    val userUiState: MutableStateFlow<NetworkResponse<List<UserDetailEntity>>> = _userUiState

    private val _weatherUiState = MutableStateFlow<NetworkResponse<Weather>>(NetworkResponse.Empty)
    val weatherUiState: MutableStateFlow<NetworkResponse<Weather>> = _weatherUiState
    private val weatherService = RetroFitInstance.weatherServiceGetInstance
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
                        _userUiState.value = NetworkResponse.Success(dbData)
                    } else {
                        Log.d(TAG, "unsuccessful request "+"body is null")
                    }
                } else {
                    _userUiState.value = NetworkResponse.Error(response.message())
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "exception occurred when getting from api ${e.message}")
            }
        }
    }
    fun searchByName(query: String){
        viewModelScope.launch {
            try {
                var dbData:  List<UserDetailEntity>
                if(query.trim().isEmpty()){
                    dbData = userDetailRepository.getAllUsersDetail()
                } else {
                     dbData = userDetailRepository.getFilteredUsers(query)
                }
                _userUiState.value = NetworkResponse.Success(dbData)
            } catch (e: Exception){
                Log.d(TAG, "exception occurred when filtering out ${e.message}")
            }
        }
    }
    fun getWeatherDetail(lat: Double, lon: Double){
        _weatherUiState.value = NetworkResponse.Loading
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
                    weatherUiState.value = NetworkResponse.Error(response.message())
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "error occurred "+e.message.toString())
                NetworkResponse.Error(e.message.toString())
            }
        }
    }

    @SuppressLint("ServiceCast")
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }



}