package com.android.marvel.ui.repo

import com.android.marvel.ui.api.ApiService
import com.android.marvel.ui.model.CharactersResponse
import com.android.marvel.ui.model.ComicsResponse
import com.android.marvel.ui.model.SeriesResponse

class MarvelRepositoryImp(
    private val apiService: ApiService,
) : MarvelRepository {

    override suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse {
        return apiService.getCharacters(limit, offset)
    }

    override suspend fun getComicsByCharacterId(id: String): ComicsResponse {
        return apiService.getComicsByCharacterId(id)
    }

    override suspend fun getSeriesByCharacterId(id: String): SeriesResponse {
        return apiService.getSeriesByCharacterId(id)
    }

    override suspend fun getEventByCharacterId(id: String): ComicsResponse {
        return apiService.getEventByCharacterId(id)
    }

    override suspend fun getStoriesByCharacterId(id: String): SeriesResponse {
        return apiService.getStoriesByCharacterId(id)
    }

    override suspend fun searchByName(nameStartsWith: String): CharactersResponse {
        return apiService.searchByName(nameStartsWith)
    }
}

