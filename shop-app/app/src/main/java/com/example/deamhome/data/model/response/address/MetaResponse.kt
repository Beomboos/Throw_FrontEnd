package com.example.deamhome.data.model.response.address

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(
    val total_count: Int,
    val pageable_count: Int,
    val is_end: Boolean,
)
