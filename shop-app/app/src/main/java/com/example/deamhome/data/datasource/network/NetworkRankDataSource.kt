package com.example.deamhome.data.datasource.network

import com.example.deamhome.data.model.request.RankerStoreRequest
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.data.model.response.RankingResponse
import com.example.deamhome.data.retrofit.RankService
import com.example.deamhome.domain.model.ApiResponse

class NetworkRankDataSource(
    private val service: RankService
) {
    suspend fun leaderboard(): ApiResponse<List<LeaderboardResponse>>{
        return service.leaderboard()
    }

    suspend fun rankerStore(request: RankerStoreRequest): ApiResponse<List<RankerStore>>{
        return service.rankerStore(request)
    }

    suspend fun ranking(): ApiResponse<RankingResponse>{
        return service.ranking()
    }
}