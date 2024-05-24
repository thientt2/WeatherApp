package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class Astronomy(
    @SerializedName("sunrise") var sunrise: String? = null,
    @SerializedName("sunset") var sunset: String? = null,
)