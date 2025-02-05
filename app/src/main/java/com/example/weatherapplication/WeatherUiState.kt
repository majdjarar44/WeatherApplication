package com.example.weatherapplication

import com.example.weatherapplication.data.model.WeatherResponse

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherResponse? = null,
    val errorMessage: String? = null
)
