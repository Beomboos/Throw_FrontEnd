package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface StoreService {
    @POST("store/name")
    fun getStoresInfoBySearchName(
        @Body request: StoreSearchNameRequest,
    ): ApiResponse<List<StoreInfoBySearchNameResponse>>

    @POST("store/search")
    fun getStoreInfoBySearchLocation(
        @Body request: StoreSearchLocationRequest,
    ): ApiResponse<List<StoresInfoByLocationResponse>>
}