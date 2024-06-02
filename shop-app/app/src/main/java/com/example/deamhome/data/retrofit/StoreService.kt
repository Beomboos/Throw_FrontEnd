package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.CrnRequest
import com.example.deamhome.data.model.request.DeleteRequest
import com.example.deamhome.data.model.request.ModifyRequest
import com.example.deamhome.data.model.request.QRRequest
import com.example.deamhome.data.model.request.RegisterRequest
import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

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

    @POST("store")
    suspend fun registerRequest(
        @Body register: RegisterRequest
    ): ApiResponse<String>

    @PUT("store")
    suspend fun modify(
        @Body request: ModifyRequest
    ): ApiResponse<StoreResponse>

    @HTTP(method = "DELETE", path = "store", hasBody = true)
    suspend fun delete(
        @Body uuid: DeleteRequest
    ): ApiResponse<Unit>

    @POST("qr/makeqr")
    suspend fun qr(
        @Body request: QRRequest
    ): ApiResponse<ResponseBody>

    @POST("qr/mileageup")
    suspend fun mileageUpdate(
        @Body request: QRRequest
    ): ApiResponse<Unit>
}