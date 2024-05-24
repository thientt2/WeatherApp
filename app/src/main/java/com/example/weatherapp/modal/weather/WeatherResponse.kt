package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data") var data: WeatherData? = WeatherData()
)
