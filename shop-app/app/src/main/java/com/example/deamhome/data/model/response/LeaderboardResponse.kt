package com.example.deamhome.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardResponse(
    val inputId: String,
    val userName: String,
    val mileage: Int,
    val ranking: Int,
)
