package com.example.deamhome.data.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileModifyRequest(
    @SerializedName("userPhoneNumber")val userPhoneNumber: String,
    @SerializedName("email")val email: String,
    @SerializedName("userName")val userName: String,
)
