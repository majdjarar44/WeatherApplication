package com.example.weatherapplication.common

import com.example.weatherapplication.data.model.WeatherResponse

interface WeatherObserver {
    fun update(weatherData: WeatherResponse?)
}