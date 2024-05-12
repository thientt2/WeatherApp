package com.example.weatherapp

sealed class Screen(val screen: String) {
    data object Home: Screen("home")
    data object Advanced: Screen("advanced")

}