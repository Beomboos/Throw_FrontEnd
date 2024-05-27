package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val inputId: String,
    val inputPassword: String,
    val inputPasswordCheck: String,
    val sns: String,
    val userStatus: String,
    val role: String,
    val userName: String,
    val userPhoneNumber: String,
    val email: String
)
