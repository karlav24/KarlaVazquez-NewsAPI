package com.example.newsapi.data
import com.example.newsapi.EndpointService
import com.example.newsapi.Repository
import com.example.newsapi.data.NewsEndpoint
import com.example.newsapi.data.ArticleResponses
import com.example.newsapi.data.SourceResponses

class NewsRepoImpl(
    endpointService: EndpointService
) : Repository<NewsEndpoint>(endpointService), NewsRepo {

    override suspend fun getSources(
        country: String?,
        category: String?
    ): Result<SourceResponses> {
        return try {
            Result.success(getEndpoint().getSources(country, category))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEverything(
        sources: String?,
        page: Int,
        pageSize: Int
    ): Result<ArticleResponses> {
        return try {
            Result.success(getEndpoint().getEverything(sources, page, pageSize))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getEverythingForCategory(
        category: String,
        page: Int,
        pageSize: Int
    ):Result<ArticleResponses> {
        return try {
            Result.success(getEndpoint().getEverythingForCategory(category, page, pageSize))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
