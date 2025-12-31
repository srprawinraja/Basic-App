package com.example.basicapp.api.service

import com.example.basicapp.data.User.Users
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("api")
    suspend fun getAllUserDetails(@Query("results") results: Int): Response<Users>

}