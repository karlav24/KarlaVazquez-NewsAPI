package com.example.newsapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import com.example.newsapi.NewsApiService

class NewsViewModel : ViewModel() {
    private val newsApiService = RetrofitInstance.getRetrofitInstance()
        .create(NewsApiService::class.java)

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    fun fetchNews(country: String, category: String, apiKey: String) {
        viewModelScope.launch {
            val response = newsApiService.getTopHeadlines(country, category, apiKey)
            if (response.isSuccessful) {
                _articles.value = response.body() ?: emptyList()
            } else {
                // Handle error
                _articles.value = emptyList()
                val errorCode: Int = response.code()
                println("API call failed with error code: $errorCode")
            }
            }
        }
    }

