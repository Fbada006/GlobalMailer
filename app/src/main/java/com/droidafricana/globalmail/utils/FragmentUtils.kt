package com.droidafricana.globalmail.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.databinding.FragmentSearchBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.view.adapter.MyArticleAdapter
import com.droidafricana.globalmail.viewModel.search.ArticleApiStatus
import com.squareup.picasso.Picasso

const val TAG = "FragmentUtils"

object FragmentUtils {
    /**
     * @param owner         is the owner of the lifecycle
     * @param articlesFromDb is the LiveData to observe
     * @param articleAdapter is the adapter
     */
    fun observeForArticlesFromDb(owner: LifecycleOwner, articlesFromDb: LiveData<List<Article>>,
                                 articleAdapter: MyArticleAdapter) {
        articlesFromDb.observe(owner, Observer { articleList ->
            if (articleList != null) {
                articleAdapter.submitList(articleList)
            }
        })
    }

    /**
     * @param owner         is the owner of the lifecycle
     * @param status is the LiveData to observe with the loading status
     * @param binding is the binding instance for the views
     */
    fun observeViewModelForLoadingStatus(context: Context?, owner: LifecycleOwner, status: LiveData<ArticleApiStatus>,
                                         binding: FragmentSearchBinding) {
        status.observe(owner, Observer { articleApiStatus ->
            when (articleApiStatus) {
                ArticleApiStatus.LOADING -> {
                    binding.typing.visibility = View.GONE
                    binding.layoutNoArticlesFound.visibility = View.GONE
                    binding.avProgress.visibility = View.VISIBLE
                }
                ArticleApiStatus.DONE -> {
                    binding.typing.visibility = View.GONE
                    binding.layoutNoArticlesFound.visibility = View.GONE
                    binding.avProgress.visibility = View.GONE
                }
                ArticleApiStatus.ERROR -> {
                    binding.typing.visibility = View.GONE
                    binding.layoutNoArticlesFound.visibility = View.VISIBLE
                    binding.avProgress.visibility = View.GONE
                    showErrorToast(context, context?.resources!!.getString(R.string.failed_please_retry))
                }
                else -> {
                }
            }
        })
    }

    private fun showErrorToast(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

//    /**
//     * @param owner         is the owner of the lifecycle
//     * @param articleViewModel is the ViewModel with LiveData to observe
//     * @param articleAdapter is the adapter
//     */
//    fun refreshUI(application: Application, owner: Fragment, articlesFromDb: LiveData<List<Article>>,
//                  articleAdapter: MyArticleAdapter, binding: MySportsNewsFragmentBinding,
//                  articleViewModel: SportsViewModel) {
//
//        binding.layoutSwipeRefresh.setOnRefreshListener {
//            //Make the API call
//            articleViewModel.refreshSportsDataInDb(application)
//
//            //Observe the LiveData object again and update the UI
//            observeForArticlesFromDb(owner, articlesFromDb, articleAdapter)
//
//            //Hide the refreshing indicator
//            binding.layoutSwipeRefresh.isRefreshing = false
//        }
//    }

    /*This method listens to scrolls and determines if Picasso should load images*/
    fun listenToUserScrolls(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE ||
                        newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    //Load the images because user is no longer scrolling
                    Picasso.get().resumeTag(PicassoTags.ARTICLE_TAG)
                } else {
                    //No point loading the images if the user is not looking at the articles
                    Picasso.get().pauseTag(PicassoTags.ARTICLE_TAG)
                }
            }
        })
    }
}
