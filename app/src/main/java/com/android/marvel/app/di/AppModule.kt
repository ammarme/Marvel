package com.android.marvel.app.di

import com.android.marvel.app.api.ApiConfig
import com.android.marvel.app.api.ApiService
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.repo.MarvelRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMarvelRepository(apiService: ApiService): MarvelRepository {
        return MarvelRepositoryImp(apiService)
    }

    @Provides
    fun provideApiService(): ApiService {
        return ApiConfig.provideApiService()
    }
}
