package com.example.weatherapplication.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherFetcher()
    }

    @Provides
    @Singleton
    fun provideWeatherFetcher(): WeatherFetcher {
        return WeatherFetcher()
    }
}