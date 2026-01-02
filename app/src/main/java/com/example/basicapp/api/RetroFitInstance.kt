package com.example.basicapp.api

import com.example.basicapp.api.service.UserService
import com.example.basicapp.api.service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroFitInstance{
    const val BASE_URL_USER: String = "https://randomuser.me/"
    const val BASE_URL_WEATHER: String = "https://api.openweathermap.org/data/2.5/"
    private val userRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_USER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_WEATHER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userServiceGetInstance: UserService = userRetrofit.create(UserService::class.java)
    val weatherServiceGetInstance: WeatherService = weatherRetrofit.create(WeatherService::class.java)
}
