package com.android.marvel.app.repo

import com.android.marvel.app.api.ApiService
import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.model.DetailsResponse

class MarvelRepositoryImp(
    private val apiService: ApiService,
) : MarvelRepository {

    override suspend fun getCharacters(limit: Int, offset: Int): CharactersResponse {
        return apiService.getCharacters(limit, offset)
    }

    override suspend fun getComicsByCharacterId(id: String): DetailsResponse {
        return apiService.getComicsByCharacterId(id)
    }

    override suspend fun getSeriesByCharacterId(id: String): DetailsResponse {
        return apiService.getSeriesByCharacterId(id)
    }

    override suspend fun getEventByCharacterId(id: String): DetailsResponse {
        return apiService.getEventByCharacterId(id)
    }

    override suspend fun getStoriesByCharacterId(id: String): DetailsResponse {
        return apiService.getStoriesByCharacterId(id)
    }

    override suspend fun searchByName(nameStartsWith: String): CharactersResponse {
        return apiService.searchByName(nameStartsWith)
    }
}