package com.droidafricana.globalmail.ui.health

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.ArticleClickListenerInterface
import com.droidafricana.globalmail.adapter.MyArticleAdapter
import com.droidafricana.globalmail.databinding.MyHealthNewsFragmentBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.ui.SettingsFragment
import com.droidafricana.globalmail.ui.favs.FavsViewModel
import com.droidafricana.globalmail.utils.CustomTabsUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.utils.PrefUtils
import com.droidafricana.globalmail.utils.getArticleViewModelFactory
import com.like.LikeButton

class HealthNewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: MyHealthNewsFragmentBinding
    private val mHealthViewModel by viewModels<HealthViewModel> {
        getArticleViewModelFactory(category = PrefUtils.categoryHealth(requireContext())!!)
    }

    private lateinit var mApplication: Application
    private val mFavsViewModel by viewModels<FavsViewModel> {
        getArticleViewModelFactory(category = "")
    }

    private var favsList: List<Article> = ArrayList()

    private val mMyArticleAdapter = MyArticleAdapter(object : ArticleClickListenerInterface {
        override fun onArticleClick(article: Article) {
            CustomTabsUtils.launchCustomTabs(context, article.articleUrl)
        }

        override fun liked(likeButton: LikeButton?, article: Article) {
            mHealthViewModel.insertArticleIntoFavs(article)
        }

        override fun unLiked(likeButton: LikeButton?, article: Article) {
            mHealthViewModel.deleteArticleFromFavs(article)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.my_health_news_fragment, container, false)

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

        mHealthViewModel.healthArticlesFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mMyArticleAdapter.submitList(it)

                for (article in favsList) {
                    article.isLiked = article in it
                }
            }
        })

        checkIfPreferencesHaveBeenUpdated()

        //Assign the fragmentViewModel to the articleViewModel
        binding.healthFragmentViewModel = mHealthViewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragment_search -> {
                findNavController().navigate(
                        HealthNewsFragmentDirections.actionHealthNewsDestToActivitySearchDest(
                                PrefUtils.categoryHealth(context!!)!!)
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
        mHealthViewModel.refreshHealthDataInDb(mApplication)

        //Observe the LiveData object again and update the UI
        FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mHealthViewModel.healthArticlesFromDb
                , mMyArticleAdapter)

        //Hide the refreshing indicator
        binding.layoutSwipeRefresh.isRefreshing = false

    }

    /*Check if settings have changed and so the UI should be refreshed by making a new API call*/
    private fun checkIfPreferencesHaveBeenUpdated() {
        //If they have been updated, make another API call and update the UI
        if (SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED) {
            //Try to make the API call
            mHealthViewModel.refreshHealthDataInDb(mApplication)

            //Observe the LiveData object again and update the UI
            FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mHealthViewModel.healthArticlesFromDb
                    , mMyArticleAdapter)
        }
        //Set the flag back to false to avoid the refresh being a one time thing
        SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED = false
    }
}
