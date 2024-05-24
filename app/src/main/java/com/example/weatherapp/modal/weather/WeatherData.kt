package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("current_condition") var currentCondition: CurrentCondition? = CurrentCondition(),
    @SerializedName("weather") var weather: List<Weather>? = listOf()
)
