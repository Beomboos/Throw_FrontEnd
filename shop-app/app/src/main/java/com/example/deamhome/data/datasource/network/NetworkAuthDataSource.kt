package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.request.LoginRequest
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.data.retrofit.AuthService
import com.example.deamhome.domain.model.ApiResponse

class NetworkAuthDataSource(
    private val service: AuthService,
) {
    suspend fun refreshToken(): ApiResponse<Token> {
        return service.refreshToken()
    }
//
//    suspend fun signUp(signUpRequest: SignUpRequest): ApiResponse<String> {
//        return apolloService.signUp(signUpRequest)
//    }

    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token> {
        return service.login(loginRequest)
    }
}
