package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.request.RankerStoreRequest
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.data.model.response.RankingResponse
import com.example.deamhome.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RankService {
    @GET("rank/leaderboard")
    suspend fun leaderboard(): ApiResponse<List<LeaderboardResponse>>

    @POST("rank/store")
    suspend fun rankerStore(
        @Body request: RankerStoreRequest
    ): ApiResponse<List<RankerStore>>

    @GET("rank")
    suspend fun ranking(): ApiResponse<RankingResponse>
}