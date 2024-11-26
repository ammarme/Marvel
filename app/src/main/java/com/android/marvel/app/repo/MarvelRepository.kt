package com.android.marvel.app.repo

import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.model.DetailsResponse

interface MarvelRepository {
    suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse
    suspend fun getComicsByCharacterId(id: String): DetailsResponse
    suspend fun getSeriesByCharacterId(id: String): DetailsResponse
    suspend fun getEventByCharacterId(id: String): DetailsResponse
    suspend fun getStoriesByCharacterId(id: String): DetailsResponse
    suspend fun searchByName(nameStartsWith: String): CharactersResponse
}