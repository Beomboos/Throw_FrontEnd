package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MailSendRequest(
    val email: String
)
