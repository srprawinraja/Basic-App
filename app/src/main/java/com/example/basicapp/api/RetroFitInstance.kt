package com.example.basicapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroFitInstance{
    const val BASE_URL: String = "https://randomuser.me/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val getInstance = retrofit.create(UserService::class.java)
}