package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class QRRequest(
    val extStoreId: String,
)
