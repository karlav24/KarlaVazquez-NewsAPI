package com.example.newsapi.data

import com.google.gson.annotations.SerializedName

data class SourceResponses(
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("status")
    val status: String
)