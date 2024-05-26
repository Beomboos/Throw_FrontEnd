package com.example.deamhome.data.model.response.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AddressModel(
    val address_name: String?,
    val b_code: String?,
    val h_code: String?,
    val mountain_yn: String?,
    val main_address_no: String?,
    val region_1depth_name: String?,
    val region_2depth_name: String?,
    val region_3depth_h_name: String?,
    val region_3depth_name: String?,
    val sub_address_no: String?,
    val x: String?,
    val y: String?,
): Parcelable
