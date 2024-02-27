package com.example.newsapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapi.articleInfo.ArticleContentFragment
import com.example.newsapi.articleList.ArticleListFragment
import com.example.newsapi.common.BaseFragment
import com.example.newsapi.data.Article
import com.example.newsapi.data.Source
import com.example.newsapi.sourceList.SourceListFragment

class MainActivity : AppCompatActivity(), BaseFragment.ActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSources()

    }

    override fun showSources() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SourceListFragment.newInstance(), TAG_SOURCE_LIST)
            .commit()
    }

    override fun showArticles(source: Source) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("article")
            .replace(
                R.id.fragment_container,
                ArticleListFragment.newInstance(source),
                TAG_ARTICLE_LIST
            )
            .commit()
    }

    override fun showContent(article: Article) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("content")
            .replace(
                R.id.fragment_container,
                ArticleContentFragment.newInstance(article),
                TAG_ARTICLE_CONTENT
            )
            .commit()
    }

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    companion object {
        const val TAG_SOURCE_LIST = "FRAG_SOURCES"
        const val TAG_ARTICLE_LIST = "FRAG_ARTICLES"
        const val TAG_ARTICLE_CONTENT = "FRAG_CONTENT"
    }
}