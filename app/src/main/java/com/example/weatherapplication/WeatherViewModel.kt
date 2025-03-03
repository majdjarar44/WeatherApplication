package com.example.weatherapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.common.WeatherDisplay
import com.example.weatherapplication.data.WeatherFetcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherFetcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        getWeather()
        startLiveUpdates(3000L)
        weatherRepository.addObserver(observer = WeatherDisplay("display 1"))
        weatherRepository.addObserver(observer = WeatherDisplay("display 2"))
        weatherRepository.addObserver(observer = WeatherDisplay("display 3"))
    }


    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = WeatherUiState(isLoading = true)
            try {

                val data = weatherRepository.fetchWeather()
                if (data.error.isNullOrEmpty()) {
                    _uiState.value = WeatherUiState(weatherData = data)
                } else {
                    _uiState.value = WeatherUiState(errorMessage = data.error)
                }

            } catch (e: Exception) {
                _uiState.value = WeatherUiState(errorMessage = "Failed to load weather data")
            }
        }
        weatherRepository.removeObserver(observer = WeatherDisplay("display 3"))

    }

    fun startLiveUpdates(intervalMillis: Long) {
        weatherRepository.startLiveUpdates(intervalMillis)
    }
}