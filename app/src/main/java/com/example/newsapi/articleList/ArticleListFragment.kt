package com.example.newsapi.articleList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.common.BaseFragment
import com.example.newsapi.R
import com.example.newsapi.data.Source
import com.example.newsapi.articleList.ArticleListAdapter
import com.example.newsapi.common.NetworkState
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class ArticleListFragment : BaseFragment() {
    private val viewModel: ArticleListViewModel by stateViewModel()
    private lateinit var adapter: ArticleListAdapter

    private lateinit var pbLoading: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var rvArticles: RecyclerView
    private lateinit var spinnerCategories: Spinner

    private val source: Source? by lazy {
        arguments?.getSerializable(ARG_SOURCE) as? Source?
    }

    private val sourceId: String
        get() = source?.id ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.article_list_fragment, container, false)
        with(view) {
            pbLoading = findViewById(R.id.pbLoading)
            tvError = findViewById(R.id.tvError)
            rvArticles = findViewById(R.id.rvArticles)
            spinnerCategories = findViewById(R.id.categorySpinner)
            setupSpinner()
        }
        val source: Source? = arguments?.getSerializable(ARG_SOURCE) as? Source
        source?.let {
            viewModel.setCurrentSource(it)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.fetchArticles(sourceId)
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent?.getItemAtPosition(position) as String
                viewModel.fetchArticlesForCategory(selectedCategory, sourceId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.updateTitle(source?.name ?: "Articles")
    }

    private fun setupRecyclerView() {
        adapter = ArticleListAdapter { article ->
            Log.d("Articles", "Clicked on ${article.title}")
            activityCallback?.showContent(article)
        }
        rvArticles.adapter = adapter
        rvArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = linearManager.findLastVisibleItemPosition()

                if (lastVisibleItem >= adapter.itemCount - 5) {
                    viewModel.fetchArticles(sourceId)
                }
            }
        })
    }
    private fun setupSpinner() {
        val categories = arrayOf("Business", "Technology", "Sports", "Entertainment", "General")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = adapter

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                // Perform actions when a category is selected, like fetching articles for that category
                viewModel.fetchArticlesForCategory(selectedCategory, sourceId)
                Log.d("CATEGORY", selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }
    }
    private fun setupObservers() {
        viewModel.apply {
            networkStateLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    NetworkState.SUCCESS -> {
                        pbLoading.visibility = View.GONE
                        rvArticles.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    NetworkState.FAILURE -> {
                        pbLoading.visibility = View.GONE
                        rvArticles.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                    }
                    NetworkState.LOADING -> {
                        pbLoading.visibility = View.VISIBLE
                        rvArticles.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    else -> {
                        pbLoading.visibility = View.GONE
                    }
                }
            })

            articlesLiveData.observe(viewLifecycleOwner, { articles ->
                adapter.setArticles(articles)
            })
        }
    }

    companion object {
        const val ARG_SOURCE = "NewsListFragment.ARG_SOURCE"

        fun newInstance(source: Source) = ArticleListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_SOURCE, source)
            }
        }
    }
}