package com.emt.elimupay.models

data class FeesResponse(
    val entity: List<Any>,
    val message: String,
    val status: Int
)