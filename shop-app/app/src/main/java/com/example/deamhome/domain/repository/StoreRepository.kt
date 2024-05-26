package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.request.ModifyRequest
import com.example.deamhome.data.model.request.RegisterRequest
import com.example.deamhome.data.model.response.RegisterResponse
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse

interface StoreRepository {
    suspend fun storeSearchName(name: String): ApiResponse<List<StoreInfoBySearchNameResponse>>
    suspend fun storeSearchLocation(lat: Double, lon: Double, dis: Double, trash: String): ApiResponse<List<StoresInfoByLocationResponse>>
    suspend fun store(): ApiResponse<List<StoreResponse>>
    suspend fun crn(crn: String): ApiResponse<StoreResponse>
    suspend fun register(register: RegisterRequest): ApiResponse<String>
    suspend fun modify(modify: ModifyRequest): ApiResponse<StoreResponse>
    suspend fun delete(extStoreId: String): ApiResponse<Unit>
}