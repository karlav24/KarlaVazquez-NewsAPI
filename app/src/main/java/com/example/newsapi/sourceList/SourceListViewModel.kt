package com.example.newsapi.sourceList

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapi.common.BaseViewModel
import com.example.newsapi.common.NetworkState
import com.example.newsapi.data.ArticleResponses
import com.example.newsapi.data.NewsRepo
import com.example.newsapi.data.Source
import com.example.newsapi.data.SourceResponses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.Result

class SourceListViewModel(
    private val newsRepository: NewsRepo,
    @get:VisibleForTesting
    internal val networkCallContext: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private val _sourcesLiveData = MutableLiveData<List<Source>>()

    @VisibleForTesting
    internal var errorCount = 0
    val sourcesLiveData: LiveData<List<Source>>
        get() = _sourcesLiveData

    fun fetchSources() {
        _networkStateLiveData.value = NetworkState.LOADING
        viewModelScope.launch(networkCallContext) {
            val result: Result<SourceResponses> = newsRepository.getSources(null, null)
            if(result.isSuccess){
                _sourcesLiveData.postValue(result.getOrNull()?.sources ?: emptyList())
                _networkStateLiveData.postValue(NetworkState.SUCCESS)
                errorCount = 0
            }
            else{
                if (errorCount > 2) {
                    _networkStateLiveData.postValue(NetworkState.IDLE)
                    return@launch
                }
                errorCount++
                _networkStateLiveData.postValue(NetworkState.FAILURE)
            }
        }
    }
    fun fetchArticlesForCategory(selectedCategory: String) {
        viewModelScope.launch(networkCallContext) {
            val result: Result<SourceResponses> = newsRepository.getSources(null, selectedCategory)

            //val result: Result<ArticleResponses> = newsRepository.getEverythingForCategory(selectedCategory, currentPage)
            if (result.isSuccess) {
                val sources = result.getOrNull()?.sources ?: emptyList()
                val filteredSources = sources.filter { source ->

                    source.category.uppercase() == selectedCategory.uppercase() // Assuming category is a property of the Article class
                }
                //list.addAll(result.getOrNull()?.articles ?: emptyList())
                _sourcesLiveData.postValue(filteredSources)
                _networkStateLiveData.postValue(NetworkState.SUCCESS)
            } else {
                Log.e("ArticleList", "Error fetching articles for category: $selectedCategory", result.exceptionOrNull())
                if (_sourcesLiveData.value?.isEmpty() == false) {
                    _networkStateLiveData.postValue(NetworkState.IDLE)
                } else {
                    _networkStateLiveData.postValue(NetworkState.FAILURE)
                }
            }
        }
    }
}