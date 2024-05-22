package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkKakaoDataSource
import com.example.deamhome.data.model.response.AddressResponse
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.KakaoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultKakaoRepository(
    private val networkKakaoDataSource: NetworkKakaoDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): KakaoRepository {
    override suspend fun addressSearch(key: String, query: String): ApiResponse<AddressResponse> {
        return withContext(dispatcher){
            val response = networkKakaoDataSource.addressSearch(
                key = key, query = query
            )

            Log.d("HTTP_LOG", response.toString())

            response
        }
    }
}