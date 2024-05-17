package com.emt.elimupay.models

data class MpesaResponse(
    val amount_paid: String,
    val payment_date: String,
    val paymentmode: String,
    val reference: String,
    val student: String
)