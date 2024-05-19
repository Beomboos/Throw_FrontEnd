package com.example.deamhome.data.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerializedName("inputId") val inputId: String,
    @SerializedName("inputPassword") val inputPassword: String,
)