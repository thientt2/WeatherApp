package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class WeatherDesc(
    @SerializedName("value") var value: String? = null
)