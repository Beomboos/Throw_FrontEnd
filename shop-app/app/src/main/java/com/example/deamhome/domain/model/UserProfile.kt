package com.example.deamhome.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val inputId: String,
    val role: String,
    val userName: String,
    val userPhoneNumber: String,
    val email: String,
    val mileage: Int,
) {
    companion object {
        val EMPTY = UserProfile("", "", "","", "", 0)
    }
}
