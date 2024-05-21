package com.emt.elimupay.models

data class BalanceResponseX(
    val balance: Double,
    val credit: Int,
    val debit: Double,
    val first_name: String,
    val student_id: Int,
    val unique_id: String
)