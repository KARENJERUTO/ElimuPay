package com.emt.elimupay.api

import com.emt.elimupay.models.LoginRequest
import com.emt.elimupay.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiCalls {
    @POST("auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>
}
