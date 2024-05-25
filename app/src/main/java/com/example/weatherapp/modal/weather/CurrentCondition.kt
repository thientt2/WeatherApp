package com.example.weatherapp.modal.weather


data class CurrentCondition(
    val observation_time: String,
    val temp_C: String,
    val temp_F: String,
    val weatherCode: String,
    val weatherIconUrl: List<WeatherIconUrl>,
    val weatherDesc: List<WeatherDesc>,
    val windspeedMiles: String,
    val windspeedKmph: String,
    val winddirDegree: String,
    val winddir16Point: String,
    val precipMM: String,
    val precipInches: String,
    val humidity: String,
    val visibility: String,
    val visibilityMiles: String,
    val pressure: String,
    val pressureInches: String,
    val cloudcover: String,
    val FeelsLikeC: String,
    val FeelsLikeF: String,
    val uvIndex: String
)