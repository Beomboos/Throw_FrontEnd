package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(
    val extStoreId: String
)
