package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.response.address.AddressResponse
import com.example.deamhome.domain.model.ApiResponse

interface KakaoRepository {
    suspend fun addressSearch(key: String, query: String): ApiResponse<AddressResponse>
}