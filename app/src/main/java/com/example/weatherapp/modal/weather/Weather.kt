package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Weather(
    @SerializedName("date") var date: LocalDateTime? = null,
    @SerializedName("astronomy") var astronomy: Astronomy? = Astronomy(),
    @SerializedName("maxtempC") var maxtempC: Int? = null,
    @SerializedName("mintempC") var mintempC: Int? = null,
    @SerializedName("hourly") var hourly: List<HourlyWeather>? = listOf()
)