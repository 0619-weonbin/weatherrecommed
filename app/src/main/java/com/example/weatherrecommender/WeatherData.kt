package com.example.weatherrecommender

data class WeatherData(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float
)

data class Weather(
    val description: String
)