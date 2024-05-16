package com.emt.elimupay.models

data class login(
    val address: String,
    val date_joined: String,
    val date_of_birth: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val groups: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: Any,
    val last_name: String,
    val middle_name: String,
    val nationality: String,
    val password: String,
    val roles: List<Any>,
    val schools: Int,
    val user_permissions: List<Any>,
    val usergroup: Int,
    val username: String
)