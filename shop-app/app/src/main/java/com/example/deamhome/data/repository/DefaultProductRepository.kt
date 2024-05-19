package com.example.deamhome.data.repository

import com.example.deamhome.data.datasource.network.NetworkProductDataSource
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
}
