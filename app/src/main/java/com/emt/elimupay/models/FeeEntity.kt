package com.emt.elimupay.models

data class FeeEntity(
    val balance: Int,
    val credit: Int,
    val debit: Int,
    val description: String,
    val id: Int,
    val student_id: Int,
    val transaction_date: String
)