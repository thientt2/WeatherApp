package com.example.weatherapp.viewmodal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.modal.NewsItem
import com.example.weatherapp.network.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdvancedViewModel : ViewModel() {
    private val _newsItems = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsItems: StateFlow<List<NewsItem>> get() = _newsItems

    private val _newsWorldItems = MutableStateFlow<List<NewsItem>>(emptyList())

    val newsWorldItems: StateFlow<List<NewsItem>> get() = _newsWorldItems
    private var job: Job? = null
    private var jobWorld: Job? = null
    fun fetchNewsByCategory(category: String = "top") {
        job = viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLatestNews("vi", category, "pub_44426632a2d70cbb0c1d54e91dab73cce58a9")
                _newsItems.value = response.results
                println("success: ${response.results}")
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun fetchNewsWorld() {
        jobWorld = viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLatestNews("vi", "world", "pub_44426632a2d70cbb0c1d54e91dab73cce58a9")
                _newsWorldItems.value = response.results
                println("success: ${response.results}")
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        jobWorld?.cancel()
    }
}