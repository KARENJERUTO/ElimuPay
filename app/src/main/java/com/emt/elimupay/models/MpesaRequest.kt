package com.emt.elimupay.models

data class MpesaRequest(
    val amount: Double,
    val phoneNumber: String,
    val accountReference: String,
    val transactionDesc: String
)
