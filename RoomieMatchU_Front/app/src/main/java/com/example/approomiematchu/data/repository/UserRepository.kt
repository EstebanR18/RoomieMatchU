package com.example.approomiematchu.data.repository

import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.LoginRequest
import com.example.approomiematchu.data.remote.dto.LoginResponse
import com.example.approomiematchu.data.remote.dto.RegisterRequest
import com.example.approomiematchu.data.remote.dto.RegisterResponse
import com.example.approomiematchu.data.remote.dto.UserResponse

class UserRepository(
    private val api: ApiService = RetrofitClient.instance
) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error en login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error en registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verificarPerfil(userId: Long): Boolean {
        return try {
            val response = api.obtenerPerfil(userId)
            response.isSuccessful // true si existe (200), false si 404
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserById(userId: Long): Result<UserResponse> {
        return try {
            val response = api.getUserById(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al obtener usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
