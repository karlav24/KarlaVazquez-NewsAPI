package com.example.newsapi
import com.google.gson.annotations.SerializedName

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    @SerializedName("urlToImage") val imageUrl: String?,
    @SerializedName("publishedAt") val publishedDate: String?,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String
)

data class NewsResponse(
    val articles: List<Article>
)
