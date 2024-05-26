package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.response.address.AddressResponse
import com.example.deamhome.domain.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {
    @GET("v2/local/search/address.json")
    suspend fun getSearchAddress(
        @Header("Authorization") key: String,
        @Query("query") query: String,
    ): ApiResponse<AddressResponse>
}