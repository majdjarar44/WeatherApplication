package com.example.weatherapplication.data

import android.util.Log
import com.example.weatherapplication.common.WeatherSubject
import com.example.weatherapplication.data.model.WeatherResponse
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Timer
import java.util.TimerTask

class WeatherFetcher : WeatherSubject(), WeatherRepository {

    private val gson = Gson()

    override fun fetchWeather(): WeatherResponse {
        return try {
            val urlString =
                "https://api.open-meteo.com/v1/forecast?latitude=31.95&longitude=35.93&current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m"
            val url = URL(urlString)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val content = reader.readText()

            reader.close()
            conn.disconnect()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val weatherResponse =
                    gson.fromJson(content, WeatherResponse::class.java) // Using Gson
                notifyObservers(weatherResponse)
                return weatherResponse
            } else {
                WeatherResponse(error ="Failed: HTTP $conn.responseCode" )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            WeatherResponse(error = "Error: ${e.message}")
        }
    }

    override fun startLiveUpdates(intervalMillis: Long) {
        val timer = Timer(true)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchWeather() // Fetch weather data periodically
            }
        }, (60 * 60 * 1000), intervalMillis)
    }
}
