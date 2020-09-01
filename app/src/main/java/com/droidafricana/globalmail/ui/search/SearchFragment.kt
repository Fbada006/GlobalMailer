package com.droidafricana.globalmail.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.SearchArticleAdapter
import com.droidafricana.globalmail.adapter.SearchArticleClickListenerInterface
import com.droidafricana.globalmail.databinding.FragmentSearchBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.utils.CustomTabsUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.utils.getArticleViewModelFactory

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private val searchFragmentArgs: SearchFragmentArgs by navArgs()
    lateinit var binding: FragmentSearchBinding

    private val mMyArticleAdapter = SearchArticleAdapter(object : SearchArticleClickListenerInterface {
        override fun onArticleClick(article: Article) {
            CustomTabsUtils.launchCustomTabs(context, article.articleUrl)
        }
    })

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
        val viewModel by viewModels<SearchViewModel> {
            getArticleViewModelFactory(searchFragmentArgs.categoryString, query)
        }

        viewModel.getLiveArticles(context, searchFragmentArgs.categoryString, query)

        viewModel.liveArticlesObservable.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.layoutNoArticlesFound.visibility = View.VISIBLE
            } else {
                binding.layoutNoArticlesFound.visibility = View.GONE
            }
            mMyArticleAdapter.submitList(it)
            binding.typing.visibility = View.GONE

        })

        FragmentUtils.observeViewModelForLoadingStatus(context, viewLifecycleOwner,
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
