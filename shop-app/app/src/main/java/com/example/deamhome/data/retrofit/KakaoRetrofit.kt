package com.example.deamhome.data.retrofit

import android.util.Log
import com.example.deamhome.BuildConfig
import com.example.deamhome.data.CallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class KakaoRetrofit {
    companion object {
        private val HTTP_LOG_TAG = "HTTP_LOG"

        fun createInstance(): Retrofit {
            val jsonConfig = Json { ignoreUnknownKeys = true }
            return Retrofit.Builder()
                .baseUrl(BuildConfig.KAKAO_SERVER_URL)
                .client(createOkHttpClient())
                .addCallAdapterFactory(CallAdapterFactory())
                .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        private fun createOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(getHttpLoggingInterceptor())
            }.build()
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}