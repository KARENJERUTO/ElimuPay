package com.emt.elimupay.models

data class Entity(
    val date_joined: String,
    val email: String,
    val id: Int,
    val is_active: Boolean,
    val is_admin: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val is_verified: Boolean,
    val last_login: String,
    val password: String,
    val username: String
)