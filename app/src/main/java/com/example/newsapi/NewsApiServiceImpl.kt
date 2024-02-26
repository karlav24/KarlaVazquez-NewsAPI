package com.example.newsapi

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import com.example.newsapi.RetrofitInstance.getRetrofitInstance

class NewsApiServiceImpl: NewsApiService {
    private val newsApi: NewsApiService
    init{
        val retrofit: Retrofit = getRetrofitInstance()
        newsApi = retrofit.create(NewsApiService::class.java)
    }
    override suspend fun getTopHeadlines(
        country: String,
        category: String,
        apiKey: String
    ): Response<List<Article>>{
        return newsApi.getTopHeadlines(country, category, apiKey)
        /*val response: Response<List<Article>> = call.awaitResponse()
        if(response.isSuccessful){
            return response.body()?: emptyList()
        }
        else{
            emptyList<String>()
        }
        return emptyList()
         */
    }
}