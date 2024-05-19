package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkProductDataSource
import com.example.deamhome.data.model.request.ProfileModifyRequest
import com.example.deamhome.data.model.response.Message
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultProductRepository(
    private val networkProductDataSource: NetworkProductDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ProductRepository {
    override suspend fun user(): ApiResponse<UserProfile> {
        return withContext(dispatcher){
            val response = networkProductDataSource.user()

            response
        }
    }

    override suspend fun logout(): ApiResponse<Unit> {
        return withContext(dispatcher){
            val response = networkProductDataSource.logout()

            Log.d(HTTP_LOG_TAG, response.toString())
            if(response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG, "Success")
            }
            else if(response is ApiResponse.NetworkError){
                Log.d(HTTP_LOG_TAG, "NetworkError")
            }
            else if(response is ApiResponse.Unexpected){
                Log.d(HTTP_LOG_TAG, "Unexpected")
            }

            response
        }
    }

    override suspend fun modify(name: String, phone: String, email: String): ApiResponse<Unit> {
        return withContext(dispatcher){
            val response = networkProductDataSource.modify(
                ProfileModifyRequest(
                    userName = name,
                    userPhoneNumber = phone,
                    email = email
                )
            )

            response
        }
    }

    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}
