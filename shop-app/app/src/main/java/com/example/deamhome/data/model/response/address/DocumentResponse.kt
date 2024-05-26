package com.example.deamhome.data.model.response.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DocumentResponse(
    val address: AddressModel?,
    val address_name: String?,
    val address_type: String?,
    val road_address: RoadAddressModel?,
    val x: String?,
    val y: String?,
):Parcelable
