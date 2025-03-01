package com.example.screenvibe.di

import com.example.screenvibe.BuildConfig
import com.example.screenvibe.data.api.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    //TODO: room , retrofit

    @Provides
    fun provideBaseUrl() : String =  "https://api.themoviedb.org/3/"

    @Provides
    fun provideApiKey(): String = BuildConfig.TMDB_API_KEY

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKey : String) : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

            val apiKeyInterceptor = Interceptor { chain ->
                val original = chain.request()
                val originalUrl = original.url
                val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("api_key",apiKey )
                .build()
                val requestWithApiKey = original.newBuilder()
                    .url(urlWithApiKey)
                    .build()

                chain.proceed(requestWithApiKey)
            }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl : String, okHttpClient : OkHttpClient) : Retrofit = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(
        GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideApiService(retrofit : Retrofit) : TmdbApiService = retrofit.create(TmdbApiService::class.java)



}