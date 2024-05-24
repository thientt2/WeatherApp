package com.example.weatherapp.network

import com.example.weatherapp.constant.Const.Companion.apiWeatherKey
import com.example.weatherapp.modal.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather.ashx")
    suspend fun getWeather(
        @Query("q") location: String = "",
        @Query("key") apiKey: String = apiWeatherKey,
        @Query("num_of_days") numOfDays: Int = 7,
        @Query("tp") tp: Int = 1,
        @Query("format") format: String = "json"
    ): WeatherResponse
}