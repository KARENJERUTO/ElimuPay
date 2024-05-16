package com.emt.elimupay.retrofit

import LoginRequest
import com.emt.elimupay.ResetPasswordRequest
import com.emt.elimupay.models.BalanceResponse
import com.emt.elimupay.models.ResetPasswordResponse
import com.emt.elimupay.models.login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/resetPassword")
    fun resetPassword(@Body req: ResetPasswordRequest): Call<ResetPasswordResponse>

    @GET("fee/api/v1/fee/categories/get_categories_for_student/1/")
    fun getBalance(): Call<BalanceResponse>
    @POST("auth/login")
    fun login(@Body req:LoginRequest): Call<login>

}
