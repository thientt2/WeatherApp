package com.example.weatherapp.modal.location

data class City(
    val lat: String,
    val lon: String,
    val name: String,
    val display_name: String
){
    override fun toString(): String {
        return "$lat,$lon,$name,$display_name"
    }

    companion object {
        fun fromString(cityString: String): City {
            val parts = cityString.split(",")
            return City(parts[0], parts[1], parts[2], parts[3])
        }
    }
}
