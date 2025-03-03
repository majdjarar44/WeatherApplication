package com.example.weatherapplication.data

import com.example.weatherapplication.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun fetchWeather(): WeatherResponse
    fun startLiveUpdates(intervalMillis: Long)
}