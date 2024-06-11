package com.emt.elimupay.models

data class FeeEntity(
    val id: Int,
    val student_id: Int,
    val description: String,
    val debit: Int,
    val credit: Int,
    val balance: Int,
    val transaction_date: String,
    val firstName: String,
    val middleName: String
)
