package com.emt.elimupay.api

import LoginRequest
import com.emt.elimupay.LoginResponse
import com.emt.elimupay.ResetPasswordRequest
import com.emt.elimupay.models.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiCalls {
    @POST("auth/login")
    fun login(@Body req:LoginRequest): Call<LoginResponse>

    @POST("auth/resetPassword")
    fun resetPassword(@Body req: ResetPasswordRequest): Call<ResetPasswordResponse>
}




