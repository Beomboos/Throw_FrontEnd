package com.example.deamhome.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String,
    val refreshToken: String,
    val grantType: String = "",
    val expiresIn: Int = 0,
) {
    companion object {
        const val EMPTY = ""
        val EMPTY_TOKEN = Token(EMPTY, EMPTY, EMPTY, 0)
    }
}
