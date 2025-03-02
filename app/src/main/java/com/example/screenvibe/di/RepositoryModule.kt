package com.example.screenvibe.di

import com.example.screenvibe.data.api.TmdbApiService
import com.example.screenvibe.data.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(apiService: TmdbApiService): MoviesRepository {
        return MoviesRepository(apiService)
    }
}