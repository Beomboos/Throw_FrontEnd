package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkMailDataSource
import com.example.deamhome.data.model.request.MailConfirmRequest
import com.example.deamhome.data.model.request.MailSendRequest
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.MailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultMailReposity(
    private val networkMailDataSource: NetworkMailDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): MailRepository {
    override suspend fun mailSend(email: String): ApiResponse<Unit> {
        return withContext(dispatcher){
            val response = networkMailDataSource.mailSend(
                MailSendRequest(
                    email=email
                )
            )

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else {
                Log.d(HTTP_LOG_TAG,response.toString())
            }

            response
        }
    }

    override suspend fun mailConfirm(authCode: String, email: String): ApiResponse<Unit> {
        return withContext(dispatcher){
            val response = networkMailDataSource.mailConfirm(
                MailConfirmRequest(
                    authCode = authCode,
                    email = email
                )
            )

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else {
                Log.d(HTTP_LOG_TAG, response.toString())
            }

            response
        }
    }

    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}