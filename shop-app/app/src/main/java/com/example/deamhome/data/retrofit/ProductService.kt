package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.response.Message
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {
    @GET("user")
    suspend fun user(): ApiResponse<UserProfile>

    @POST("login/logout")
    suspend fun logout(): ApiResponse<Message>
}