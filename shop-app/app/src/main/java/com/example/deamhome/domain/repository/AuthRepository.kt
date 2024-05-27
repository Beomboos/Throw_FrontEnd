package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.request.SignUpRequest
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.domain.model.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLogin: Boolean
    suspend fun login(id: String, pwd: String): ApiResponse<Token>
    fun getToken(): Token
    fun refreshToken()
    fun removeToken()
    fun updateToken(token: Token)
    suspend fun signUp(id: String, pwd: String, pwdCheck: String, name: String, phone: String, email: String): ApiResponse<Unit>
}
