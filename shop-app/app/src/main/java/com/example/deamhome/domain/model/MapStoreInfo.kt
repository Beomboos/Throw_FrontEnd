package com.example.deamhome.domain.model

data class MapStoreInfo(
    val id: String,
    val storeName: String,
    val storePhone: String,
    val storeFullAddress: String,
    val geoPoint: GeoPoint,
    val storeImageUrl: String = "https://www.bizhankook.com/upload/bk/article/202002/thumb/19402-44437-sampleM.jpg",
)
