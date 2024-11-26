package com.android.marvel.ui.api

import com.android.marvel.BuildConfig
import com.android.marvel.ui.model.CharactersResponse
import com.android.marvel.ui.model.ComicsResponse
import com.android.marvel.ui.model.SeriesResponse
import com.android.marvel.ui.utils.HashGenerate
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): CharactersResponse

    @GET("/v1/public/characters")
    suspend fun searchByName(
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): CharactersResponse

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getComicsByCharacterId(
        @Path("characterId") characterId: String
    ): ComicsResponse

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getSeriesByCharacterId(
        @Path("characterId") characterId: String
    ): SeriesResponse

    @GET("/v1/public/characters/{characterId}/events")
    suspend fun getEventByCharacterId(
        @Path("characterId") characterId: String
    ): ComicsResponse

    @GET("/v1/public/characters/{characterId}/stories")
    suspend fun getStoriesByCharacterId(
        @Path("characterId") characterId: String
    ): SeriesResponse

    object Api {

        // Create OkHttpClient
        private fun createOkHttpClient(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            return OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .addInterceptor { chain -> createParametersDefault(chain) }
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }

        // Add default parameters to each request
        private fun createParametersDefault(chain: Interceptor.Chain): Response {
            val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            var request = chain.request()
            val builder = request.url.newBuilder()

            builder.addQueryParameter("apikey", BuildConfig.API_PUBLIC)
                .addQueryParameter(
                    "hash",
                    HashGenerate.generate(timeStamp, BuildConfig.API_PRIVATE, BuildConfig.API_PUBLIC)
                )
                .addQueryParameter("ts", timeStamp.toString())

            request = request.newBuilder().url(builder.build()).build()
            return chain.proceed(request)
        }

        // Create Retrofit instance
        private fun createRetrofit(): Retrofit {
            val okHttpClient = createOkHttpClient()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        // Create ApiService
        fun createApiService(): ApiService {
            val retrofit = createRetrofit()
            return retrofit.create(ApiService::class.java)
        }
    }

}

