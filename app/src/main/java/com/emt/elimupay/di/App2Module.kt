package com.emt.elimupay.di

import com.emt.elimupay.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object App2Module {
    private const val BASE_URL = "http://192.168.88.198:8000/api/v1/"

    val apiService: ApiService by lazy {
        provideApiService()
    }

    private fun provideApiService(): ApiService {
        val client = createOkHttpClient()
        val retrofit = createRetrofit(client)
        return retrofit.create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
