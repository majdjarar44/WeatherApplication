package com.example.weatherapplication.common

import android.util.Log
import com.example.weatherapplication.data.model.WeatherResponse

class WeatherDisplay(private val displayId: String) : WeatherObserver {

    override fun update(weatherData: WeatherResponse?) {
        Log.d("Display Weather For Each Subject" , "$displayId"+"${weatherData}")
    }
}