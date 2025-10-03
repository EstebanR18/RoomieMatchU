package com.example.approomiematchu.data.repository

import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.NewPasswordRequest
import com.example.approomiematchu.data.remote.dto.ResetEmailRequest
import com.example.approomiematchu.data.remote.dto.ValidateTokenRequest

class AuthRepository(
    private val api: ApiService = RetrofitClient.instance
) {

    // Paso 1: solicitar código
    suspend fun requestCode(correo: String): Result<String> {
        return try {
            val response = api.requestPasswordReset(ResetEmailRequest(correo))
            if (response.isSuccessful) {
                // Si el backend envía {"mensaje":"..."} o un String plano
                val mensaje = extractMessage(response.body()) ?: "Código enviado correctamente"
                Result.success(mensaje)
            } else {
                val errorMsg = cleanError(response.errorBody()?.string())
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Paso 2: validar código
    suspend fun validateToken(correo: String, token: String): Result<String> {
        return try {
            val response = api.validateToken(ValidateTokenRequest(correo, token))
            if (response.isSuccessful) {
                val mensaje = extractMessage(response.body()) ?: "Código validado correctamente"
                Result.success(mensaje)
            } else {
                val errorMsg = cleanError(response.errorBody()?.string())
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Paso 3: restablecer contraseña
    suspend fun resetPassword(correo: String, token: String, newPassword: String): Result<String> {
        return try {
            val response = api.resetPassword(NewPasswordRequest(correo, token, newPassword))
            if (response.isSuccessful) {
                val mensaje = extractMessage(response.body()) ?: "Contraseña actualizada correctamente"
                Result.success(mensaje)
            } else {
                val errorMsg = cleanError(response.errorBody()?.string())
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---- Helpers ----

    // Limpia errores con llaves, comillas, etc.
    private fun cleanError(raw: String?): String {
        if (raw.isNullOrBlank()) return "Ocurrió un error desconocido"
        return raw
            .replace("{", "")
            .replace("}", "")
            .replace("\"", "")
            .replace("mensaje:", "")
            .trim()
    }

    // Extrae mensaje si el backend envía {"mensaje":"..."} o texto plano
    private fun extractMessage(body: Any?): String? {
        return when (body) {
            is String -> body // texto plano
            is Map<*, *> -> body["mensaje"] as? String // JSON con clave mensaje
            else -> null
        }
    }
}



