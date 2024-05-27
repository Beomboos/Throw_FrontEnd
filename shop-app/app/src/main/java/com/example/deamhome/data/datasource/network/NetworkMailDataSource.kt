package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.request.MailConfirmRequest
import com.example.deamhome.data.model.request.MailSendRequest
import com.example.deamhome.data.retrofit.MailService
import com.example.deamhome.domain.model.ApiResponse

class NetworkMailDataSource(
    private val service: MailService
) {
    suspend fun mailConfirm(request: MailConfirmRequest): ApiResponse<Unit>{
        return service.mailConfirm(request)
    }

    suspend fun mailSend(request: MailSendRequest): ApiResponse<Unit>{
        return service.mailSend(request)
    }
}