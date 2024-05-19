package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkProductDataSource
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

    override suspend fun logout(): ApiResponse<Message> {
        return withContext(dispatcher){
            val response = networkProductDataSource.logout()

            Log.d("HTTP_LOG", response.toString())
            if(response is ApiResponse.Success) {
                Log.d("HTTP_LOG", "Success")
            }
            else if(response is ApiResponse.NetworkError){
                Log.d("HTTP_LOG", "NetworkError")
            }
            else if(response is ApiResponse.Unexpected){
                Log.d("HTTP_LOG", "Unexpected")
            }

            response
        }
    }
}
