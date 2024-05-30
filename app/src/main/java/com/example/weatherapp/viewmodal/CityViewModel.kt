package com.example.weatherapp.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.modal.location.City
import com.example.weatherapp.modal.location.CityRespone
import com.example.weatherapp.modal.location.CitySearchRespone
import com.example.weatherapp.modal.location.LocationData
import com.example.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityViewModel: ViewModel() {
    private val _city = MutableStateFlow<List<City>?>(null)
    val city: StateFlow<List<City>?> get() = _city

    private val _cityLatLon = MutableStateFlow<CityRespone?>(null)
    val cityLatLon: StateFlow<CityRespone?> get() = _cityLatLon

    fun fetchCity(cityName: String){
        viewModelScope.launch {
            try{
                val respone = RetrofitInstance.apiCity.getCityName(cityName)
                _city.value = respone

            } catch (e: Exception){
                println("false city------------------")
                e.printStackTrace()
            }
        }

    }

    fun fetchCityByLatLon(lat: Double, lon: Double){
        viewModelScope.launch {
            try{
                val respone = RetrofitInstance.apiCity.getCityNameByLatLon(lat,lon)
                _cityLatLon.value = respone
                println("Success city api: ${respone}")
            } catch (e: Exception){
                println("false city by lat lon------------------")
                e.printStackTrace()
            }
        }
    }
}