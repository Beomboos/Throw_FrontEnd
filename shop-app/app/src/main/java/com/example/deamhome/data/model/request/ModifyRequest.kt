package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ModifyRequest(
    val extStoreId: String,
    val storePhone: String,
    val crn: String,
    val latitude: Double,
    val longitude: Double,
    val zipCode: String,
    val fullAddress: String,
    val trashType: String
)
