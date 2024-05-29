package com.emt.elimupay.models

data class MpesaRequest(
    val amount: String,
    val phoneNumber: String,
    val accountReference: String,

)
