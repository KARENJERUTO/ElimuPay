package com.emt.elimupay.api

import com.emt.elimupay.models.BalanceResponse
import com.emt.elimupay.models.FeesResponse
import com.emt.elimupay.models.MpesaResponse
import com.emt.elimupay.models.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/resetPassword")
    fun resetPassword(): Call<ResetPasswordResponse>

    @GET("fee/api/v1/fee/categories/get_categories_for_student/1/")
    fun getBalance(): Call<BalanceResponse>

    @GET("feecollections/fee-collections/")
    fun getFees(): Call<FeesResponse>

    @POST("payfee/fee-collection/")
    fun initiateMpesaPayment(
        @Query("studentUniqueID") studentUniqueID: String,
        @Query("amount") amount: String,
        @Query("phoneNumber") phoneNumber: String
    ): Call<MpesaResponse>
}
