package com.android.marvel.app.api

import com.android.marvel.BuildConfig
import com.android.marvel.app.utils.HashGenerate
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    // Create OkHttpClient
    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(ApiConfig::addDefaultParameters)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    // Add default parameters to each request
    private fun addDefaultParameters(chain: Interceptor.Chain): Response {
        val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        val originalRequest = chain.request()
        val urlWithParams = originalRequest.url.newBuilder()
            .addQueryParameter("apikey", BuildConfig.API_PUBLIC)
            .addQueryParameter(
                "hash",
                HashGenerate.generate(timeStamp, BuildConfig.API_PRIVATE, BuildConfig.API_PUBLIC)
            )
            .addQueryParameter("ts", timeStamp.toString())
            .build()

        val newRequest = originalRequest.newBuilder().url(urlWithParams).build()
        return chain.proceed(newRequest)
    }

    // Create Retrofit instance
    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide ApiService
    fun provideApiService(): ApiService {
        return createRetrofit().create(ApiService::class.java)
    }
}
