package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.data.retrofit.StoreService
import com.example.deamhome.domain.model.ApiResponse

class NetworkStoreDataSource(
    private val service: StoreService
) {
    suspend fun searchName(storeSearchNameRequest: StoreSearchNameRequest): ApiResponse<List<StoreInfoBySearchNameResponse>>{
        return service.getStoresInfoBySearchName(storeSearchNameRequest);
    }
    suspend fun searchLocation(storeSearchLocationRequest: StoreSearchLocationRequest): ApiResponse<List<StoresInfoByLocationResponse>>{
        return service.getStoreInfoBySearchLocation(storeSearchLocationRequest)
    }
}