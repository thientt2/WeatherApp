package com.example.weatherapp.network

import com.example.weatherapp.modal.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("latest")
    suspend fun getLatestNews(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apikey") apiKey: String
    ): NewsResponse
}