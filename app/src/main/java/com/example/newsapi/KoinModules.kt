package com.example.newsapi

import com.example.newsapi.data.NewsRepo
import com.example.newsapi.data.NewsRepoImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*
import com.example.newsapi.articleInfo.ArticleContentViewModel
import com.example.newsapi.articleList.ArticleListViewModel
import com.example.newsapi.sourceList.SourceListViewModel

val serviceModule = module {
    single { EndpointService() }
    single(named("ApiDateFormat")) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
}

val repositoryModule = module {
    single<NewsRepo> { NewsRepoImpl(get()) }
}

val viewModelModule = module {
    viewModel { SourceListViewModel(get()) }
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleContentViewModel(get(named("ApiDateFormat"))) }
}

val appModules = listOf(serviceModule, repositoryModule, viewModelModule)