package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.response.AddressResponse
import com.example.deamhome.data.retrofit.KakaoService
import com.example.deamhome.domain.model.ApiResponse

class NetworkKakaoDataSource(
    private val service: KakaoService
) {
    suspend fun addressSearch(key: String, query: String): ApiResponse<AddressResponse> {
        return service.getSearchAddress(key, query);
    }
}