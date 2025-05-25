package com.example.guess_the_car.di

import android.content.Context
import com.example.guess_the_car.data.local.CarDatabase
import com.example.guess_the_car.data.remote.CarApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCarDatabase(
        @ApplicationContext context: Context
    ): CarDatabase = CarDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideCarDao(database: CarDatabase) = database.carDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/") // Base URL for raw GitHub content
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCarApiService(retrofit: Retrofit): CarApiService {
        return retrofit.create(CarApiService::class.java)
    }
} 