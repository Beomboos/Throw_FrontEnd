package com.example.deamhome.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val storePhone: String, // 가게 전화번호
    val crn: String, // 사업자등록번호
    val latitude: Double, // 위도 (-90~90)
    val longitude: Double, // 경도 (-180~180)
    val zipCode: String, // 우편번호
    val fullAddress: String, // 지번주소
    val trashType: String, // 일쓰->병->플라스틱->종이->캔
)
