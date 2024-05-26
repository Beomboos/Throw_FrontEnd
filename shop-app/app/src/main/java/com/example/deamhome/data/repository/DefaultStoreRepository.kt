package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkStoreDataSource
import com.example.deamhome.data.model.request.CrnRequest
import com.example.deamhome.data.model.request.DeleteRequest
import com.example.deamhome.data.model.request.ModifyRequest
import com.example.deamhome.data.model.request.RegisterRequest
import com.example.deamhome.data.model.request.StoreSearchLocationRequest
import com.example.deamhome.data.model.request.StoreSearchNameRequest
import com.example.deamhome.data.model.response.RegisterResponse
import com.example.deamhome.data.model.response.StoreResponse
import com.example.deamhome.data.model.response.StoreInfoBySearchNameResponse
import com.example.deamhome.data.model.response.StoresInfoByLocationResponse
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.StoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultStoreRepository(
    private val networkStoreDataSource: NetworkStoreDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): StoreRepository {
    override suspend fun storeSearchName(name: String): ApiResponse<List<StoreInfoBySearchNameResponse>>{
        return withContext(dispatcher){
            val response = networkStoreDataSource.searchName(
                StoreSearchNameRequest(
                    storeName = name
                )
            )

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            else if(response is ApiResponse.Failure){
                Log.d(HTTP_LOG_TAG, response.toString());
            }

            response
        }
    }

    override suspend fun storeSearchLocation(lat: Double, lon: Double, dis: Double, trash: String): ApiResponse<List<StoresInfoByLocationResponse>> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.searchLocation(
                StoreSearchLocationRequest(
                    latitude = lat,
                    longitude = lon,
                    distance = dis,
                    trashType = trash
                )
            )

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            else if(response is ApiResponse.Failure){
                Log.d(HTTP_LOG_TAG, response.toString());
            }

            response
        }
    }

    override suspend fun store(): ApiResponse<List<StoreResponse>> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.store()

            if(response is ApiResponse.Success){
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else
                Log.d(HTTP_LOG_TAG, response.toString());

            response
        }
    }

    override suspend fun crn(crn: String): ApiResponse<StoreResponse> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.crn(
                CrnRequest(
                    crn=crn
                )
            )

            response
        }
    }

    override suspend fun register(register: RegisterRequest): ApiResponse<String> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.register(
                RegisterRequest(
                    storePhone = register.storePhone,
                    crn = register.crn,
                    latitude = register.latitude,
                    longitude = register.longitude,
                    zipCode = register.zipCode,
                    fullAddress = register.fullAddress,
                    trashType = register.trashType
                )
            )

            if(response is ApiResponse.Success){
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else
                Log.d(HTTP_LOG_TAG, response.toString());

            response
        }
    }

    override suspend fun delete(extStoreId: String): ApiResponse<Unit> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.delete(
                DeleteRequest(
                    extStoreId = extStoreId
                )
            )

            if(response is ApiResponse.Success){
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else
                Log.d(HTTP_LOG_TAG, response.toString());

            response
        }
    }

    override suspend fun modify(modify: ModifyRequest): ApiResponse<StoreResponse> {
        return withContext(dispatcher){
            val response = networkStoreDataSource.modify(
                ModifyRequest(
                    extStoreId = modify.extStoreId,
                    storePhone = modify.storePhone,
                    crn = modify.crn,
                    latitude = modify.latitude,
                    longitude = modify.longitude,
                    zipCode = modify.zipCode,
                    fullAddress = modify.fullAddress,
                    trashType = modify.trashType
                )
            )

            if(response is ApiResponse.Success){
                Log.d(HTTP_LOG_TAG,"Success")
            }
            else
                Log.d(HTTP_LOG_TAG, response.toString());

            response
        }
    }
    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}