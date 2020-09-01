package com.droidafricana.globalmail.ui.general

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.ArticleClickListenerInterface
import com.droidafricana.globalmail.adapter.MyArticleAdapter
import com.droidafricana.globalmail.databinding.MyGeneralNewsFragmentBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.ui.SettingsFragment
import com.droidafricana.globalmail.ui.favs.FavsViewModel
import com.droidafricana.globalmail.utils.CustomTabsUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.utils.PrefUtils
import com.droidafricana.globalmail.utils.getArticleViewModelFactory
import com.like.LikeButton

class TopNewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private val TAG = "TopNewsFragment"
    lateinit var binding: MyGeneralNewsFragmentBinding
    private val mGeneralViewModel by viewModels<GeneralViewModel> {
        getArticleViewModelFactory(PrefUtils.categoryGeneral(requireContext()))
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
            mGeneralViewModel.insertArticleIntoFavs(article)
        }

        override fun unLiked(likeButton: LikeButton?, article: Article) {
            mGeneralViewModel.deleteArticleFromFavs(article)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MyGeneralNewsFragmentBinding.inflate(
                inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        //Get the general list
        mGeneralViewModel.generalArticlesFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.e(TAG, "onCreateView:--------------- $it ")
                mMyArticleAdapter.submitList(it)

                for (article in favsList) {
                    article.isLiked = article in it
                }
            }
        })

        checkIfPreferencesHaveBeenUpdated()

        //Assign the fragmentViewModel to the articleViewModel
        binding.fragmentViewModel = mGeneralViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragment_search -> {
                findNavController().navigate(
                        TopNewsFragmentDirections.actionTopNewsDestToActivitySearchDest(PrefUtils.categoryGeneral(context!!)!!)
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

        //Try to make the API call
        mGeneralViewModel.refreshGeneralDataInDb(mApplication)

        //Observe the LiveData object again and update the UI
        FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mGeneralViewModel.generalArticlesFromDb, mMyArticleAdapter)

        //Hide the refreshing indicator
        binding.layoutSwipeRefresh.isRefreshing = false
    }

    /*Check if settings have changed and so the UI should be refreshed by making a new API call*/
    private fun checkIfPreferencesHaveBeenUpdated() {
        //If they have been updated, make another API call and update the UI
        if (SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED) {
            //Try to make the API call
            mGeneralViewModel.refreshGeneralDataInDb(mApplication)

            //Observe the LiveData object again and update the UI
            FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mGeneralViewModel.generalArticlesFromDb, mMyArticleAdapter)
        }
        //Set the flag back to false to avoid the refresh being a one time thing
        SettingsFragment.PREFERENCES_HAVE_BEEN_UPDATED = false
    }
}
