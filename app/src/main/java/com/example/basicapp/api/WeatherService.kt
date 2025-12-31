package com.example.basicapp.api

import com.example.basicapp.data.User.Users
import com.example.basicapp.data.weather.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeatherDetail(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appId") appId: String = "cede1bfcab162197de0a8735def6e25d"): Response<Weather>
}