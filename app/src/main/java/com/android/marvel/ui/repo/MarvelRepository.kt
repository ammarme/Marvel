package com.android.marvel.ui.repo

import com.android.marvel.ui.model.CharactersResponse
import com.android.marvel.ui.model.ComicsResponse
import com.android.marvel.ui.model.SeriesResponse

interface MarvelRepository {

    //remote
    suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse
    suspend fun getComicsByCharacterId(id: String): ComicsResponse
    suspend fun getSeriesByCharacterId(id: String): SeriesResponse
    suspend fun getEventByCharacterId(id: String): ComicsResponse
    suspend fun getStoriesByCharacterId(id: String): SeriesResponse
    suspend fun searchByName(nameStartsWith: String): CharactersResponse

}