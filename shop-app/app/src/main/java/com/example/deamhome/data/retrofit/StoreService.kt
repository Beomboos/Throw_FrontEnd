package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.CrnRequest
import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreService {
    @GET("store/user")
    suspend fun userStore(): ApiResponse<List<StoreResponse>>
    @POST("store/name")
    suspend fun getStoresInfoBySearchName(
        @Body request: StoreSearchNameRequest,
    ): ApiResponse<List<StoreInfoBySearchNameResponse>>

    @POST("store/search")
    suspend fun getStoreInfoBySearchLocation(
        @Body request: StoreSearchLocationRequest,
    ): ApiResponse<List<StoresInfoByLocationResponse>>

    @POST("store/crn")
    suspend fun crn(
        @Body request: CrnRequest
    ): ApiResponse<StoreResponse>
}