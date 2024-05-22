package com.example.deamhome.data.model.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    @SerializedName("documents")val documents: List<DocumentResponse>,
    @SerializedName("meta")val meta: MetaResponse,
)
@Serializable
data class MetaResponse(
    @SerializedName("total_count")val totalCnt: String,
    @SerializedName("pageable_count")val pageCnt: String,
    @SerializedName("is_end")val isEnd: String,
)
@Serializable
data class DocumentResponse(
    @SerializedName("address")val address: AddressModel,
    @SerializedName("address_name")val addressName: String,
    @SerializedName("address_type")val addressType: String,
    @SerializedName("road_address")val roadAddress: RoadAddressModel,
    @SerializedName("x")val longitude: String,
    @SerializedName("y")val latitude: String,
)
@Serializable
data class AddressModel(
    @SerializedName("address_name")val addressName: String,
    @SerializedName("h_code")val hCode: String,
    @SerializedName("b_code")val bCode: String,
    @SerializedName("mountain_yn")val mountainYN: String,
    @SerializedName("main_address_no")val mainAddressNo: String,
    @SerializedName("region_1depth_name")val region1depthName: String,
    @SerializedName("region_2depth_name")val region2depthName: String,
    @SerializedName("region_3depth_h_name")val region3depthHName: String,
    @SerializedName("region_3depth_name")val region3depthName: String,
    @SerializedName("sub_address_no")val subAddressNo: String,
    @SerializedName("x")val longitude: String,
    @SerializedName("y")val latitude: String,
)
@Serializable
data class RoadAddressModel(
    @SerializedName("address_name")val addressName: String,
    @SerializedName("building_name")val buildingName: String,
    @SerializedName("main_building_no")val mainBuildingNo: String,
    @SerializedName("region_1depth_name")val region1depthName: String,
    @SerializedName("region_2depth_name")val region2depthName: String,
    @SerializedName("region_3depth_name")val region3depthName: String,
    @SerializedName("road_name")val roadName: String,
    @SerializedName("sub_building_no")val subBuildingNo: String,
    @SerializedName("underground_yn")val undergroundYN: String,
    @SerializedName("x")val longitude: String,
    @SerializedName("y")val latitude: String,
    @SerializedName("zone_no")val zoneNo: String,
)
