package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse

interface StoreRepository {
    suspend fun storeSearchName(name: String): ApiResponse<List<StoreInfoBySearchNameResponse>>
    suspend fun storeSearchLocation(lat: Double, lon: Double, dis: Double, trash: String): ApiResponse<List<StoresInfoByLocationResponse>>
}