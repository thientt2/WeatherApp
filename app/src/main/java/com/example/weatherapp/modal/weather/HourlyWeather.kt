package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class HourlyWeather(
    @SerializedName("time") var time: String? = null,
    @SerializedName("tempC") var tempC: Int? = null,
    @SerializedName("weatherIconUrl") var iconUrl: IconUrl? = IconUrl()
)