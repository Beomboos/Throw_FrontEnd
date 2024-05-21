package com.example.deamhome.data.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CrnRequest(
    @SerializedName("crn")val crn: String
)
