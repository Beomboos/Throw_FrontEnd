package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.response.Message
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile

interface ProductRepository {
    suspend fun user(): ApiResponse<UserProfile>
    suspend fun logout(): ApiResponse<Unit>
    suspend fun modify(name: String, phone: String, email: String): ApiResponse<Unit>
}
