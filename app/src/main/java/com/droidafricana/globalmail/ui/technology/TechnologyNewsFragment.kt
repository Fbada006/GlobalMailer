package com.droidafricana.globalmail.ui.technology

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.ArticleClickListenerInterface
import com.droidafricana.globalmail.adapter.MyArticleAdapter
import com.droidafricana.globalmail.databinding.MyTechNewsFragmentBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.ui.SettingsFragment
import com.droidafricana.globalmail.ui.favs.FavsViewModel
import com.droidafricana.globalmail.utils.CustomTabsUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.utils.PrefUtils
import com.droidafricana.globalmail.utils.getArticleViewModelFactory
import com.like.LikeButton
import kotlinx.android.synthetic.main.activity_main.*

class TechnologyNewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: MyTechNewsFragmentBinding
    private val mTechViewModel by viewModels<TechnologyViewModel> {
        getArticleViewModelFactory(PrefUtils.categoryTech(requireContext()))
    }

    private lateinit var mApplication: Application
    private val mFavsViewModel by viewModels<FavsViewModel> {
        getArticleViewModelFactory()
    }
    private var favsList: List<Article> = ArrayList()

    private val mMyArticleAdapter = MyArticleAdapter(object : ArticleClickListenerInterface {
        override fun onArticleClick(article: Article) {
            CustomTabsUtils.launchCustomTabs(context, article.articleUrl)
        }

        override fun liked(likeButton: LikeButton?, article: Article) {
            mTechViewModel.insertArticleIntoFavs(article)
        }

        override fun unLiked(likeButton: LikeButton?, article: Article) {
            mTechViewModel.deleteArticleFromFavs(article)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.my_tech_news_fragment, container, false)

        setHasOptionsMenu(true)
        binding.lifecycleOwner = this

        val gridLayoutManager = StaggeredGridLayoutManager(context?.resources!!.getInteger(R.integer.column_article_count),
                StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mMyArticleAdapter

        binding.layoutSwipeRefresh.setOnRefreshListener(this)

        mApplication = this.activity!!.application

        //Set the scroll listener on the recyclerView
        FragmentUtils.listenToUserScrolls(binding.recyclerView)

        mFavsViewModel.favArticlesFromDb.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                favsList = it
            }
        })

        mTechViewModel.techArticlesFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mMyArticleAdapter.submitList(it)

                for (article in favsList) {
                    article.isLiked = article in it
                }
            }
        })

        checkIfPreferencesHaveBeenUpdated()

        //Assign the fragmentViewModel to the articleViewModel
        binding.techFragmentViewModel = mTechViewModel

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragment_search -> {
                NavHostFragment.findNavController(my_nav_host_fragment).navigate(
                        TechnologyNewsFragmentDirections.actionTechnologyNewsDestToActivitySearchDest(
                                PrefUtils.categoryTech(context!!)!!)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Called when the user swipes the layout to refresh the data
    override fun onRefresh() {
        binding.layoutSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(mApplication,
                R.color.colorAccent))

        //Make the API call
        mTechViewModel.refreshTechDataInDb(mApplication)

        //Observe the LiveData object again and update the UI
        FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mTechViewModel.techArticlesFromDb
                , mMyArticleAdapter)

        //Hide the refreshing indicator
        binding.layoutSwipeRefresh.isRefreshing = false

    }

    /*Check if settings have changed and so the UI should be refreshed by making a new API call*/
    private fun checkIfPreferencesHaveBeenUpdated() {
        //If they have been updated, make another API call and update the UI
        if (SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED) {
            //Try to make the API call
            mTechViewModel.refreshTechDataInDb(mApplication)

            //Observe the LiveData object again and update the UI
            FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mTechViewModel.techArticlesFromDb
                    , mMyArticleAdapter)
        }
        //Set the flag back to false to avoid the refresh being a one time thing
        SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED = false
    }
}
