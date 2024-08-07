package com.emt.elimupay.api

import com.emt.elimupay.StudentListResponse
import com.emt.elimupay.models.MpesaRequest
import com.emt.elimupay.models.MpesaResponse
import com.emt.elimupay.models.MyStudentsResponse
import com.emt.elimupay.models.ResetPasswordRequest
import com.emt.elimupay.models.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/resetPassword")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<ResetPasswordResponse>

//    @GET("fee/get_total_balance_for_student/1/")
//    fun getBalance(): Call<BalanceResponse>

//    @GET("fee/get_transactions_for_student/1/")
//    fun getFees(): Call<List<FeeEntity>>


    @POST("mpesa/lipa_na_mpesa/")
    fun initiateMpesaPayment(@Body request: MpesaRequest): Call<MpesaResponse>



  //  @GET("studentlist")
//  suspend fun getStudentList(@Query("parentID") parentID: Int): List<StudentListResponse>
}
