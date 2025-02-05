package com.example.weatherapplication.common

import com.example.weatherapplication.data.model.WeatherResponse


open class WeatherSubject {
    private val observers: MutableList<WeatherObserver> = ArrayList<WeatherObserver>()

    fun addObserver(observer: WeatherObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: WeatherObserver) {
        observers.remove(observer)
    }

    fun notifyObservers(weatherData: WeatherResponse?) {
        for (observer in observers) {
            observer.update(weatherData)
        }
    }
}