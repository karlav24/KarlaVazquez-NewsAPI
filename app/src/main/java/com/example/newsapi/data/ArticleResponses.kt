package com.example.newsapi.data

import com.example.newsapi.data.Article
import com.google.gson.annotations.SerializedName

data class ArticleResponses(
    @SerializedName("articles")
    val articles: ArrayList<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)