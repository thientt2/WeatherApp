package com.example.weatherapp.constant

class Const {
    companion object{
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        const val apiWeatherKey = "80392bb8718e43cbb50141324242105"

        const val LOADING = "Loading..."
        const val NA = "N/A"
        var tempCG : String = ""
        var weatherDescG :String = ""
        var weatherCodeG: String = ""
        var maxTempCG: String = ""
        var minTempCG: String = ""
    }
}