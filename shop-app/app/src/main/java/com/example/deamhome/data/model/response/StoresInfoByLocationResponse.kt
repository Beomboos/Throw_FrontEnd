package com.example.deamhome.data.model.response

import com.example.deamhome.domain.model.GeoPoint
import com.example.deamhome.domain.model.MapStoreInfo
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class StoresInfoByLocationResponse(
    @SerializedName("extStoreId") val extStoreId: String,
    @SerializedName("storeName") val storeName: String,
    @SerializedName("storePhone") val storePhone: String,
    @SerializedName("crn") val crn: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("zipCode") val zipCode: String,
    @SerializedName("fullAddress") val fullAddress: String,
    @SerializedName("trashType") val trashType: String,
) {
    fun toUI(): MapStoreInfo {
        return MapStoreInfo(
            extStoreId,
            storeName,
            storePhone,
            fullAddress,
            geoPoint = GeoPoint(latitude, longitude),
        )
    }
}
