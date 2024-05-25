package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Weather(
    val date: String,
    val astronomy: List<Astronomy>,
    val maxtempC: String,
    val maxtempF: String,
    val mintempC: String,
    val mintempF: String,
    val avgtempC: String,
    val avgtempF: String,
    val totalSnow_cm: String,
    val sunHour: String,
    val uvIndex: String,
    val hourly: List<Hourly>
)