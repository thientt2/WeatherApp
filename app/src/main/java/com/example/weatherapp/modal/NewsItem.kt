package com.example.weatherapp.modal

data class NewsItem(
    val title: String,
    val description: String,
    val link: String,
    val category: List<String>,
    val pubDate: String,
    val image_url: String?
)