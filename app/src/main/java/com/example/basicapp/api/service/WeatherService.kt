package com.example.basicapp.api.service

import androidx.core.os.BuildCompat
import com.example.basicapp.BuildConfig
import com.example.basicapp.data.weather.Weather
import com.google.gson.internal.GsonBuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherService {
    @GET("weather")
    suspend fun getWeatherDetail(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appId") appId: String =  BuildConfig.API_KEY): Response<Weather>
}