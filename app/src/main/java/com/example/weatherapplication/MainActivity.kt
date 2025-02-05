package com.example.weatherapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapplication.data.model.CurrentWeather
import com.example.weatherapplication.data.model.HourlyWeather
import com.example.weatherapplication.data.model.WeatherResponse
import com.example.weatherapplication.ui.theme.WeatherApplicationTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            WeatherApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {

                        when {
                            uiState.isLoading -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .align(Alignment.Center)

                                    )
                                }
                            }

                            uiState.weatherData != null -> {
                                uiState?.weatherData?.let {
                                    WeatherScreen(weatherData = it)
                                }
                            }

                            !uiState.errorMessage.isNullOrEmpty() -> {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = uiState.errorMessage ?: "Check Internet Connection",
                                        color = Color.Red,
                                        modifier = Modifier.align(Alignment.Center),
                                        style = TextStyle(textAlign = TextAlign.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(weatherData: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        weatherData.current?.let { CurrentWeatherCard(it) }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hourly Forecast",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(weatherData.hourly?.time?.size ?: 0) { index ->
                HourlyWeatherItem(
                    time = weatherData?.hourly?.time?.get(index) ?: "",
                    temperature = weatherData?.hourly?.temperature_2m?.get(index) ?: 0.0,
                    windSpeed = weatherData?.hourly?.wind_speed_10m?.get(index) ?: 0.0,
                    humidity = weatherData?.hourly?.relative_humidity_2m?.get(index) ?: 0.0
                )
            }
        }
    }
}

@Composable
fun CurrentWeatherCard(current: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Current Weather", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${current.temperature_2m}°C",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Wind Speed: ${current.wind_speed_10m} km/h")
        }
    }
}

@Composable
fun HourlyWeatherItem(time: String, temperature: Double, windSpeed: Double, humidity: Number) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = time, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Temp: $temperature°C", fontWeight = FontWeight.Bold)
                Text(text = "Wind: $windSpeed km/h")
                Text(text = "Humidity: $humidity%")
            }
            Icon(
                painter = painterResource(R.drawable.weather_icon),
                contentDescription = "Weather Icon",
                tint = Color.Yellow,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val sampleWeather = WeatherResponse(
        current = CurrentWeather(
            time = "2022-01-01T15:00",
            temperature_2m = 2.4,
            wind_speed_10m = 11.9
        ),
        hourly = HourlyWeather(
            time = listOf("2022-07-01T00:00", "2022-07-01T01:00"),
            wind_speed_10m = listOf(3.16, 3.02),
            temperature_2m = listOf(13.7, 13.3),
            relative_humidity_2m = listOf(82, 83)
        )
    )
    WeatherScreen(weatherData = sampleWeather)
}
