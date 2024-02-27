package com.example.newsapi.sourceList

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
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.common.BaseFragment
import com.example.newsapi.sourceList.SourceListViewModel
import com.example.newsapi.common.NetworkState
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class SourceListFragment : BaseFragment() {
    private val viewModel: SourceListViewModel by stateViewModel()
    private lateinit var adapter: SourceListAdapter

    private lateinit var pbLoading: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var rvSources: RecyclerView
    private lateinit var spinnerCategories: Spinner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.source_list_fragment, container, false)
        with(view) {
            pbLoading = findViewById(R.id.pbLoading)
            tvError = findViewById(R.id.tvError)
            rvSources = findViewById(R.id.rvSources)
            spinnerCategories = findViewById(R.id.categorySpinnerSource)
            setupSpinner()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.fetchSources()
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent?.getItemAtPosition(position) as String
                viewModel.fetchArticlesForCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.updateTitle("News Sources")
    }

    private fun setupRecyclerView() {
        adapter = SourceListAdapter { source ->
            Log.d("Sources", "Clicked on ${source.name}")
            activityCallback?.showArticles(source)
        }
        rvSources.adapter = adapter
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
                viewModel.fetchArticlesForCategory(selectedCategory)
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
                        rvSources.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }

                    NetworkState.FAILURE -> {
                        pbLoading.visibility = View.GONE
                        rvSources.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                    }

                    NetworkState.LOADING -> {
                        pbLoading.visibility = View.VISIBLE
                        rvSources.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }

                    else -> {
                        pbLoading.visibility = View.GONE
                    }
                }
            })

            sourcesLiveData.observe(viewLifecycleOwner, { sources ->
                adapter.setSources(sources)
            })
        }
    }

    @VisibleForTesting
    val rvSourcesRef
        get() = rvSources

    companion object {
        fun newInstance() = SourceListFragment()
    }
}