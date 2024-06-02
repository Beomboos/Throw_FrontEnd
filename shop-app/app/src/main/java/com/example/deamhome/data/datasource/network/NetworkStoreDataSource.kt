package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.request.CrnRequest
import com.example.deamhome.data.model.request.DeleteRequest
import com.example.deamhome.data.model.request.ModifyRequest
import com.example.deamhome.data.model.request.QRRequest
import com.example.deamhome.data.model.request.RegisterRequest
import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.RegisterResponse
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.data.retrofit.StoreService
import com.example.deamhome.domain.model.ApiResponse
import okhttp3.ResponseBody

class NetworkStoreDataSource(
    private val service: StoreService
) {
    suspend fun searchName(storeSearchNameRequest: StoreSearchNameRequest): ApiResponse<List<StoreInfoBySearchNameResponse>>{
        return service.getStoresInfoBySearchName(storeSearchNameRequest);
    }
    suspend fun searchLocation(storeSearchLocationRequest: StoreSearchLocationRequest): ApiResponse<List<StoresInfoByLocationResponse>>{
        return service.getStoreInfoBySearchLocation(storeSearchLocationRequest)
    }
    suspend fun store(): ApiResponse<List<StoreResponse>>{
        return service.userStore()
    }

    suspend fun crn(crnRequest: CrnRequest): ApiResponse<StoreResponse>{
        return service.crn(crnRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): ApiResponse<String>{
        return service.registerRequest(registerRequest)
    }

    suspend fun modify(modifyRequest: ModifyRequest): ApiResponse<StoreResponse>{
        return service.modify(modifyRequest)
    }

    suspend fun delete(deleteRequest: DeleteRequest): ApiResponse<Unit>{
        return service.delete(deleteRequest)
    }

    suspend fun qr(qrRequest: QRRequest): ApiResponse<ResponseBody>{
        return service.qr(qrRequest)
    }

    suspend fun mileageUpdate(qrRequest: QRRequest): ApiResponse<Unit>{
        return service.mileageUpdate(qrRequest)
    }
}