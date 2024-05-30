package com.example.weatherapp.viewmodal

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.modal.location.LocationData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

class LocationViewModel : ViewModel() {
    private val _location = MutableStateFlow<LocationData?>(null)
    val location: StateFlow<LocationData?> get() = _location

    fun fetchLocation(context: Context) {
        viewModelScope.launch {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val locationResult = fusedLocationClient.lastLocation.await()
                if (locationResult != null) {
                    _location.value = LocationData(locationResult.latitude, locationResult.longitude)
                }
                println("success: ${_location.value}")
            } catch (e: SecurityException) {
                // Handle exception
                println("failed location:----------------------------------------")

            }
        }
    }

    fun changeLocation(lat:Double, lon:Double){
        _location.value = LocationData(lat,lon)
    }
}