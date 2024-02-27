package com.example.newsapi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class EndpointService {

    companion object {
        const val API_KEY = "b8613d764efc48cfafbb6538c377ab74"
        const val BASE_URL = "https://newsapi.org/v2/"

        private fun getHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(::addHeaders)
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        }

        private fun addHeaders(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build()
            request = request.newBuilder()
                .url(url)
                .build()
            return chain.proceed(request)
        }
    }

    private val retrofit: Retrofit by lazy { createRetrofit() }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(getHttpClient())
            .baseUrl(BASE_URL.toHttpUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    operator fun <T> get(endpointCls: Class<T>): T = retrofit.create(endpointCls)
}
