package com.example.newsapi
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var categorySpinner: Spinner

    //class ArticleHolder{
    //    private val binding: ListItemArticleBinding
    //}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_item_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categorySpinner = view.findViewById(R.id.categorySpinner)
        val categories = listOf("business", "technology", "sports", "entertainment") // Add more categories as needed
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                fetchNewsByCategory(selectedCategory, "d8a40334f3804000bf8bd85dcd72b564")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Initialize ViewModel
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // Initialize RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.article_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = newsAdapter

        // Observe changes in articles LiveData
        newsViewModel.articles.observe(viewLifecycleOwner, Observer { articles ->
            Log.d("MainFragment", "Received ${articles.size} articles")
            newsAdapter.submitList(articles)
        })

        // Call API to fetch news initially with default category
        val apiKey = "d8a40334f3804000bf8bd85dcd72b564"
        val defaultCategory = categories[0] // Default category
        fetchNewsByCategory(defaultCategory, apiKey)
    }

    private fun fetchNewsByCategory(category: String, apiKey: String) {
        Log.d("MainFragment", "Fetching news for category: $category")
        //val country = "us"
        //newsViewModel.fetchNews(country, category, apiKey)
        val source : Source = Source("hey", "hi")
        val testArticles = listOf<Article>(
            Article(source, "Description 1", "URL 1", "wiejiowueif", "woejfwhefui", "wefuwiueufiwe", "wjehfuwhef", "eifwuefwef"),
            Article(source, "Description 2", "URL 2","wiejiowueif", "woejfwhefui", "wefuwiueufiwe", "wjehfuwhef", "eifwuefwef"),
            Article(source, "Description 3", "URL 3","wiejiowueif", "woejfwhefui", "wefuwiueufiwe", "wjehfuwhef", "eifwuefwef")
        )
        newsAdapter.submitList(testArticles)

    }

    /*private fun navigateToArticle(article: Article) {
        val action = MainFragmentDirections.actionMainFragmentToArticleFragment(article)
        findNavController().navigate(action as NavDirections)
    }*/
}
