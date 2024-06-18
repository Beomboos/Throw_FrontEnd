package com.example.deamhome.domain.repository

import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.data.model.response.RankingResponse
import com.example.deamhome.domain.model.ApiResponse

interface RankRepository {
    suspend fun leaderboard(): ApiResponse<List<LeaderboardResponse>>
    suspend fun rankerStore(inputId: String): ApiResponse<List<RankerStore>>

    suspend fun ranking(): ApiResponse<RankingResponse>
}