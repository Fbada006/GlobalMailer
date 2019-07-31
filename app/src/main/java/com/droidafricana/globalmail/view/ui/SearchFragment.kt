package com.droidafricana.globalmail.view.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.databinding.FragmentSearchBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.utils.AdapterUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.view.adapter.SearchArticleAdapter
import com.droidafricana.globalmail.view.adapter.SearchArticleClickListenerInterface
import com.droidafricana.globalmail.viewModel.search.SearchViewModel
import com.droidafricana.globalmail.viewModel.search.SearchViewModelFactory

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    lateinit var binding: FragmentSearchBinding

    private val mMyArticleAdapter = SearchArticleAdapter(object : SearchArticleClickListenerInterface {
        override fun onArticleClick(article: Article) {
            AdapterUtils.launchCustomTabs(context, article.articleUrl)
        }
    })

    private val searchFragmentArgs: SearchFragmentArgs
        get() {
            return SearchFragmentArgs.fromBundle(arguments!!)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_search, container, false
        )

        binding.toolbarSearch.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbarSearch.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.searchView.setOnQueryTextListener(this)

        binding.lifecycleOwner = this

        val gridLayoutManager = StaggeredGridLayoutManager(context?.resources!!.getInteger(R.integer.column_article_count),
                StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mMyArticleAdapter

        return binding.root
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val viewModelFactory = SearchViewModelFactory(this.activity?.application!!,
                searchFragmentArgs.categoryString, query)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        viewModel.getLiveArticles(context, searchFragmentArgs.categoryString, query)

        viewModel.liveArticlesObservable.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.layoutNoArticlesFound.visibility = View.VISIBLE
            } else {
                binding.layoutNoArticlesFound.visibility = View.GONE
            }
            mMyArticleAdapter.submitList(it)
            binding.typing.visibility = View.GONE

        })

        FragmentUtils.observeViewModelForLoadingStatus(context, this,
                viewModel.articleLoadingStatus, binding)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //Show the typing animation if there is nothing on the searchView
        if (TextUtils.isEmpty(newText)) {
            binding.typing.visibility = View.VISIBLE
        } else {
            binding.typing.visibility = View.GONE
        }
        return false
    }
}
