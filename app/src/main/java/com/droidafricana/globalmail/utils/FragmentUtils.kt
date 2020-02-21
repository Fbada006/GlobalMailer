package com.droidafricana.globalmail.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.MyArticleAdapter
import com.droidafricana.globalmail.databinding.FragmentSearchBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.ui.search.ArticleApiStatus
import com.squareup.picasso.Picasso

/**A helper object with methods for the fragments*/
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

    /**This method listens to scrolls and determines if Picasso should load images*/
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

/**Returns the [ArticleViewModelFactory] as an extension function of a fragment*/
fun Fragment.getArticleViewModelFactory(category: String? = null, queryParam: String? = null): ArticleViewModelFactory {
    val application = activity?.application!!
    return ArticleViewModelFactory(application, category, queryParam)
}
