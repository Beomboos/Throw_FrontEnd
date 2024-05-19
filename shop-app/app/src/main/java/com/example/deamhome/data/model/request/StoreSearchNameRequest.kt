package com.example.deamhome.data.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class StoreSearchNameRequest(
    @SerializedName("storeName") val storeName: String,
)
