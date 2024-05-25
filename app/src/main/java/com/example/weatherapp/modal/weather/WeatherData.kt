package com.example.weatherapp.modal.weather


data class WeatherData(
    val current_condition: List<CurrentCondition>,
    val weather: List<Weather>
)