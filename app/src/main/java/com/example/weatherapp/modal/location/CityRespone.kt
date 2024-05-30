package com.example.weatherapp.modal.location

import kotlinx.coroutines.flow.MutableStateFlow

data class CityRespone(
    val lat: String,
    val lon: String,
    val address: Address
)
