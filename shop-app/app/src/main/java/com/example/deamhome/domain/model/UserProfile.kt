package com.example.deamhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class UserProfile(
    val inputId: String,
    val role: String,
    val userName: String,
    val userPhoneNumber: String,
    val email: String,
    val mileage: Int,
): Parcelable {
    companion object {
        val EMPTY = UserProfile("", "", "","", "", 0)
    }
}
