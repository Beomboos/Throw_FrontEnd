package com.example.deamhome.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    val inputId: String,
    val userName: String,
    val mileage: Int,
    val ranking: Int,
)
