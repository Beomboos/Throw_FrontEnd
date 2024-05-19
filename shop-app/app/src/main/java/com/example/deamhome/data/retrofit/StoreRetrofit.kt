package com.example.deamhome.data.retrofit

import com.example.deamhome.data.model.response.Token

class StoreRetrofit(
    private val Token: Token
) {
    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"

        const val url = "https://moviethree.synology.me/api/"
        const val apiKey = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0aW5wdXRpZCIsImV4cCI6MTY5NDEyMTM5OCwia2luZCI6ImFjY2Vzc1Rva2VuIn0.emNvGxGVoloBYCuRL9zNh9VzP-zUIhrHFOoROfc13M2MuuyDBJ5DNV5J8xFa2O0LinfCdsSF4rM3N6flDUoufg"
    }
}