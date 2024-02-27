package com.example.newsapi.articleList

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapi.common.BaseViewModel
import com.example.newsapi.data.Article
import com.example.newsapi.data.ArticleResponses
import com.example.newsapi.data.NewsRepo
import com.example.newsapi.common.NetworkState
import com.example.newsapi.data.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.Result
import kotlin.coroutines.CoroutineContext

class ArticleListViewModel(
    private val newsRepository: NewsRepo,
    internal val networkCallContext: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private var currentPage = 1
    private val _articlesLiveData = MutableLiveData<List<Article>>()
    private var currentSource: Source? = null
    val articlesLiveData: LiveData<List<Article>>
        get() = _articlesLiveData

    fun fetchArticles(sourceId: String) {
        if (_networkStateLiveData.value?.isLoading() == true) {
            // already fetching results
            return
        }

        if (_articlesLiveData.value?.isEmpty() == false) {
            _networkStateLiveData.value = NetworkState.INTERNAL_LOADING
        } else {
            _networkStateLiveData.value = NetworkState.LOADING
        }

        viewModelScope.launch(networkCallContext) {
            val result: Result<ArticleResponses> = newsRepository.getEverything(sourceId, currentPage)
            if (result.isSuccess) {
                val list: MutableList<Article> = _articlesLiveData.value?.toMutableList() ?: mutableListOf()
                list.addAll(result.getOrNull()?.articles ?: emptyList())

                currentPage++
                _articlesLiveData.postValue(removeDuplicates(list))
                _networkStateLiveData.postValue(NetworkState.SUCCESS)
            } else {
                Log.e("ArticleList", "Error fetching from $sourceId", result.exceptionOrNull())
                if (_articlesLiveData.value?.isEmpty() == false) {
                    _networkStateLiveData.postValue(NetworkState.IDLE)
                } else {
                    _networkStateLiveData.postValue(NetworkState.FAILURE)
                }
            }
        }
    }

    fun setCurrentSource(source: Source) {
        currentSource = source
    }

    fun getCurrentSource(): Source? {
        return currentSource
    }

    fun fetchArticlesForCategory(selectedCategory: String, sourceId: String) {
        viewModelScope.launch(networkCallContext) {
            val sourceArt = getCurrentSource()
            val result: Result<ArticleResponses> = newsRepository.getEverything(sourceId,currentPage)
            //val result: Result<ArticleResponses> = newsRepository.getEverythingForCategory(selectedCategory, currentPage)
            if (result.isSuccess) {

                //val list: MutableList<Article> = _articlesLiveData.value?.toMutableList() ?: mutableListOf()
                val articles = result.getOrNull()?.articles ?: emptyList()
                val filteredArticles = articles.filter { article ->
                    sourceArt?.category == selectedCategory // Assuming category is a property of the Article class
                }
                //list.addAll(result.getOrNull()?.articles ?: emptyList())

                currentPage++
                _articlesLiveData.postValue(filteredArticles)
                _networkStateLiveData.postValue(NetworkState.SUCCESS)
            } else {
                Log.e("ArticleList", "Error fetching articles for category: $selectedCategory", result.exceptionOrNull())
                if (_articlesLiveData.value?.isEmpty() == false) {
                    _networkStateLiveData.postValue(NetworkState.IDLE)
                } else {
                    _networkStateLiveData.postValue(NetworkState.FAILURE)
                }
            }
        }
    }

    @VisibleForTesting
    internal fun removeDuplicates(list: List<Article>): List<Article> {
        val seenArticles = HashSet<String>() // Use a HashSet to store unique article titles
        val filteredList = mutableListOf<Article>()

        for (article in list) {
            val articleTitle = article.title ?: continue // Skip null titles

            if (!seenArticles.contains(articleTitle)) {
                seenArticles.add(articleTitle)
                filteredList.add(article)
            }
        }

        return filteredList
    }
}
