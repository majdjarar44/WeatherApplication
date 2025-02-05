package com.example.weatherapplication.data.model

data class WeatherResponse(

    val current: CurrentWeather? = null,

    val hourly: HourlyWeather? = null,

    val error:String ?= null
)