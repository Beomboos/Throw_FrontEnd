package com.example.deamhome.domain.repository

import com.example.deamhome.domain.model.ApiResponse

interface MailRepository {
    suspend fun mailSend(email: String): ApiResponse<Unit>
    suspend fun mailConfirm(authCode: String, email: String): ApiResponse<Unit>
}