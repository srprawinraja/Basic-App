package com.example.basicapp.api

import com.example.basicapp.data.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("api")
    suspend fun getAllUserDetails(@Query("results") results: Int): Response<Users>

}