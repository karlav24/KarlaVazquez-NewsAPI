package com.example.newsapi.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.newsapi.data.Article
import com.example.newsapi.data.Source

open class BaseFragment : Fragment() {
    var activityCallback: ActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityCallback) activityCallback = context
    }

    interface ActivityCallback {
        fun showSources()
        fun showArticles(source: Source)
        fun showContent(article: Article)
        fun updateTitle(title: String)
    }
}