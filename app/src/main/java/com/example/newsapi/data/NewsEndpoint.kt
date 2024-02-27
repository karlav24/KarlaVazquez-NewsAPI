package com.example.newsapi.data
import com.example.newsapi.data.ArticleResponses
import com.example.newsapi.data.SourceResponses
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsEndpoint {
    @GET("/v2/sources")
    suspend fun getSources(
        @Query("country") country: String?,
        @Query("category") category: String?
    ): SourceResponses

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("sources") sources: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ArticleResponses

    @GET("/v2/top-headlines")
    suspend fun getEverythingForCategory(
        @Query("category") category: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ArticleResponses

}