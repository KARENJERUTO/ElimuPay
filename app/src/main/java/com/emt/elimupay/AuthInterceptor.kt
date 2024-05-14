package com.emt.elimupay

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Base64

class AuthInterceptor(private val username: String, private val password: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("$username:$password".toByteArray()))
            .build()
        return chain.proceed(authenticatedRequest)
    }
}