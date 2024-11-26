package com.android.marvel.app.api

import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.model.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


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
    ): DetailsResponse

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getSeriesByCharacterId(
        @Path("characterId") characterId: String
    ): DetailsResponse

    @GET("/v1/public/characters/{characterId}/events")
    suspend fun getEventByCharacterId(
        @Path("characterId") characterId: String
    ): DetailsResponse

    @GET("/v1/public/characters/{characterId}/stories")
    suspend fun getStoriesByCharacterId(
        @Path("characterId") characterId: String
    ): DetailsResponse
}

