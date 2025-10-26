package com.example.approomiematchu.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileInfo(
    @SerializedName("perfilTipo") val perfilTipo: String? // Este campo viene del UserEntity
)