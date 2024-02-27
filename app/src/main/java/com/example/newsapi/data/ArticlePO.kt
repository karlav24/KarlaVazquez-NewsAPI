package com.example.newsapi.data

data class ArticlePO(
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val url: String,
    val imageUrl: String?,
    val timeSincePublish: String?
)