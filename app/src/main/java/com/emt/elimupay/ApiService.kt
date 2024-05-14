package com.emt.elimupay

import com.emt.elimupay.models.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("auth/resetPassword")
    fun resetPassword(@Body req: ResetPasswordRequest): Call<ResetPasswordResponse>

}
