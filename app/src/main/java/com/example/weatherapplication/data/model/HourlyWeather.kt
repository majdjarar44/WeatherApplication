package com.example.weatherapplication.data.model

data class HourlyWeather(

    val time: List<String>? = null,

    val wind_speed_10m: List<Double>? = null,

    val temperature_2m: List<Double>? = null,

    val relative_humidity_2m: List<Int>? = null
)