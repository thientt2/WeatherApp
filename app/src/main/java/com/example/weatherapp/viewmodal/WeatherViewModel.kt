package com.example.weatherapp.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.modal.weather.WeatherResponse
import com.example.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    private val _weather = MutableStateFlow<WeatherResponse?>(null )
    val weather: StateFlow<WeatherResponse?> get() = _weather
    fun fetchWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiWeather.getWeather("${lat},${lng}")
                _weather.value = response
                println("success1: ${response}")
            } catch (e: Exception) {
                println("fail----------------------------------")
                e.printStackTrace()
            }
        }
    }
}