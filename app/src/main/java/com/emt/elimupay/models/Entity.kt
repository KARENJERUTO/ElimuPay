package com.emt.elimupay.models

import java.io.Serializable

data class Entity(
    val address: String,
    val date_of_birth: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val last_name: String,
    val middle_name: String,
    val nationality: String,
    val roles: List<Any>,
    val schools: Int,
    val usergroup: Int
): Serializable


