package com.example.deamhome.data.model.response.address

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    val documents: List<DocumentResponse>,
    val meta: MetaResponse,
)
