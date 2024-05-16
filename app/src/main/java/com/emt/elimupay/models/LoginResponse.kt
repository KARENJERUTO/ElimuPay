package com.emt.elimupay.models

import java.io.Serializable

data class LoginResponse(
    val entity: Entity,
    val message: String,
    val status: Int
): Serializable

data class ResetPasswordResponse(
    val message: String
)
