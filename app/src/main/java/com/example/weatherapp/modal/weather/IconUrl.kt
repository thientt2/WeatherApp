package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class IconUrl(
    @SerializedName("value") var value: String? = null
)
