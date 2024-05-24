package com.example.weatherapp.viewmodal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.modal.weather.MyLatLng
import com.example.weatherapp.modal.weather.WeatherResponse
import com.example.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    var weatherRespone: WeatherResponse by mutableStateOf(WeatherResponse())
    fun getWeatherByLocaion(latLng: MyLatLng){
        viewModelScope.launch {
            try {
                val apiResponse = RetrofitInstance.apiWeather.getWeather("${latLng.lat},${latLng.lng}")
                weatherRespone = apiResponse
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}