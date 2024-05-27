package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.LoginRequest
import com.example.deamhome.data.model.request.SignUpRequest
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): ApiResponse<Token>

    @POST("login/reissue")
    suspend fun refreshToken(): ApiResponse<Token>

    @POST("user/signup")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): ApiResponse<Unit>
}