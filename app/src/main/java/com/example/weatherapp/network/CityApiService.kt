package com.example.weatherapp.network

import com.example.weatherapp.modal.location.CityRespone
import com.example.weatherapp.modal.location.CitySearchRespone
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {
    @GET("/search.php?")
    suspend fun getCityName(
        @Query("q") q: String,
        @Query("format") format: String = "jsonv2"
    ): CitySearchRespone

    @GET("reverse?")
    suspend fun getCityNameByLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("format") format: String = "json"
    ):CityRespone
}