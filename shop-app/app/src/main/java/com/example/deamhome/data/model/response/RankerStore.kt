package com.example.deamhome.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RankerStore(
    val storeName: String,
    val extStoreId: String,
)
