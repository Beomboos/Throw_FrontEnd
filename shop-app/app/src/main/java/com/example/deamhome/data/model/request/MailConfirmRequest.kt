package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MailConfirmRequest(
    val authCode: String,
    val email: String
)