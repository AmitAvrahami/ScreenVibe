package com.example.screenvibe.di

import com.example.screenvibe.BuildConfig
import com.example.screenvibe.data.api.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String = "https://api.themoviedb.org"

    @Provides
    @Named("api_key")
    fun provideApiKey(): String = BuildConfig.TMDB_API_KEY

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named("api_key") apiKey: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url
            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()
            val requestWithApiKey = original.newBuilder()
                .url(urlWithApiKey)
                .build()

            chain.proceed(requestWithApiKey)
        }

        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor) // ✅ הוספת ה- API Key Interceptor
            .addInterceptor(loggingInterceptor) // ✅ הוספת Logging Interceptor
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("base_url") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }
}