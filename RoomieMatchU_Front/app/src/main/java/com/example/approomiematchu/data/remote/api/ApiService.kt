package com.example.approomiematchu.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.approomiematchu.data.remote.dto.*

interface ApiService {

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}