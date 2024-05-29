package com.example.weatherapp.network

import com.example.weatherapp.modal.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {
    @GET("/search.php?")
    suspend fun getCityName(
        @Query("q") q: String,
        @Query("format") format: String = "jsonv2"
    ): CityRespone
}