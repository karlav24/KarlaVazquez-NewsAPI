package com.example.newsapi.common

enum class NetworkState {
    IDLE, INTERNAL_LOADING, LOADING, SUCCESS, FAILURE;

    fun isLoading(): Boolean = this == LOADING || this == INTERNAL_LOADING
}