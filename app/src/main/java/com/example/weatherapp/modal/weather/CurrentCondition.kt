package com.example.weatherapp.modal.weather

import com.google.gson.annotations.SerializedName

data class CurrentCondition(
    @SerializedName("temp_C") var tempC: Int? = null,
    @SerializedName("weatherIconUrl") var weatherIcon: IconUrl? = IconUrl(),
    @SerializedName("weatherDesc") var weatherDesc: WeatherDesc? = WeatherDesc(),
    @SerializedName("windspeedKmph") var windspeed: Int? = null,
    @SerializedName("humidity") var humidity: Double? = null,
    @SerializedName("precipMM") var precipMM: Double? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("uvIndex") var uvIndex: Int? = null,

    )