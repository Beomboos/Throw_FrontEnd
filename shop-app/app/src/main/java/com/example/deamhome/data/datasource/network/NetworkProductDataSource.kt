package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.retrofit.ProductService
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile

class NetworkProductDataSource(
    private val service: ProductService,
) {
    suspend fun user(): ApiResponse<UserProfile>{
        return service.user();
    }
}
