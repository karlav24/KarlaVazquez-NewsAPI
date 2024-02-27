package com.example.newsapi.data
import com.example.newsapi.data.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepo {

    suspend fun getSources(
        country: String?,
        category: String?
    ): Result<SourceResponses>

    suspend fun getEverything(
        sources: String?,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<ArticleResponses>
    suspend fun getEverythingForCategory(
        category: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Result<ArticleResponses>
}