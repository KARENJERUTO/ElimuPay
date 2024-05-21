package com.emt.elimupay.models

data class FeesResponse(
    val entity: List<FeeEntity>,
    val message: String,
    val status: Int
)