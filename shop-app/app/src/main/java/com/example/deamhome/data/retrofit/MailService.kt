package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.MailConfirmRequest
import com.example.deamhome.data.model.request.MailSendRequest
import com.example.deamhome.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MailService {
    @POST("auth/code")
    suspend fun mailConfirm(
        @Body request: MailConfirmRequest
    ): ApiResponse<Unit>

    @POST("auth/mail")
    suspend fun mailSend(
        @Body request: MailSendRequest
    ): ApiResponse<Unit>
}