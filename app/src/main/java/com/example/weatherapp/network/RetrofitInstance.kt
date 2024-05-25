package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    private const val BASE_URL = "https://newsdata.io/api/1/"
    private const val BASE_URL_WEATHER = "https://api.worldweatheronline.com/"

    val api: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    val apiWeather : WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}