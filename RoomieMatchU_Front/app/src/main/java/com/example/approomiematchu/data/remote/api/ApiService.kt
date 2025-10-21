package com.example.approomiematchu.data.remote.api

import retrofit2.Response
import retrofit2.http.*
import com.example.approomiematchu.data.remote.dto.*
import okhttp3.MultipartBody

interface ApiService {

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/api/password/request")
    suspend fun requestPasswordReset(@Body request: ResetEmailRequest): Response<ApiMessageResponse>

    @POST("/api/password/validate")
    suspend fun validateToken(@Body request: ValidateTokenRequest): Response<ApiMessageResponse>

    @POST("/api/password/reset")
    suspend fun resetPassword(@Body request: NewPasswordRequest): Response<ApiMessageResponse>

    @POST("/api/perfil/busco-lugar/{userId}")
    suspend fun crearPerfilBuscoLugar(
        @Path("userId") userId: Long,
        @Body request: PerfilBuscoLugarRequest
    ): Response<ApiMessageResponse>

    @POST("/api/perfil/tengo-lugar/{userId}")
    suspend fun crearPerfilTengoLugar(
        @Path("userId") userId: Long,
        @Body request: PerfilTengoLugarRequest
    ): Response<ApiMessageResponse>

    @Multipart
    @POST("/api/perfil/{userId}/foto-perfil")
    suspend fun subirFotoPerfil(
        @Path("userId") userId: Long,
        @Part file: MultipartBody.Part
    ): Response<String>
    @Multipart
    @POST("/api/perfil/{userId}/fotos-residencia")
    suspend fun subirFotosResidencia(
        @Path("userId") userId: Long,
        @Part files: List<MultipartBody.Part>
    ): Response<List<String>>

    @GET("/api/perfil/{userId}")
    suspend fun obtenerPerfil(
        @Path("userId") userId: Long
    ): Response<PerfilResponse>

}
