package com.example.deamhome.data.repository

import android.util.Log
import com.example.deamhome.data.datasource.network.NetworkRankDataSource
import com.example.deamhome.data.model.request.RankerStoreRequest
import com.example.deamhome.data.model.response.LeaderboardResponse
import com.example.deamhome.data.model.response.RankerStore
import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.repository.RankRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultRankRepository(
    private val networkRankDataSource: NetworkRankDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): RankRepository {
    override suspend fun leaderboard(): ApiResponse<List<LeaderboardResponse>> {
        return withContext(dispatcher){
            val response = networkRankDataSource.leaderboard()

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            else if(response is ApiResponse.Failure){
                Log.d(HTTP_LOG_TAG, response.toString());
            }
            response
        }
    }

    override suspend fun rankerStore(inputId: String): ApiResponse<List<RankerStore>> {
        return withContext(dispatcher){
            val response = networkRankDataSource.rankerStore(
                RankerStoreRequest(
                    inputId = inputId
                )
            )

            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            else if(response is ApiResponse.Failure){
                Log.d(HTTP_LOG_TAG, response.toString());
            }

            response
        }
    }


    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}