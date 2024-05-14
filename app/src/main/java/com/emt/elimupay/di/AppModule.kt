package com.emt.elimupay.di

import com.emt.elimupay.api.ApiCalls
import com.emt.elimupay.utils.API_BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Module{
    fun apiCalls(token: String): ApiCalls
}
class AppModule: Module {
    override fun apiCalls(token: String): ApiCalls {

        val instance: Retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val tokenInterceptor = TokenInterceptor(token)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val retInstance: ApiCalls by lazy{
            instance.create(ApiCalls::class.java)
        }

        return retInstance
    }
}

class TokenInterceptor(val token: String) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", token).build()
        return chain.proceed(request)
    }

}