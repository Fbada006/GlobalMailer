package com.droidafricana.globalmail.ui.favs

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.adapter.ArticleClickListenerInterface
import com.droidafricana.globalmail.adapter.MyArticleAdapter
import com.droidafricana.globalmail.databinding.MyFavNewsFragmentBinding
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.utils.CustomTabsUtils
import com.droidafricana.globalmail.utils.FragmentUtils
import com.droidafricana.globalmail.utils.PrefUtils
import com.droidafricana.globalmail.utils.getArticleViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.like.LikeButton

class FavsFragment : Fragment() {
    lateinit var binding: MyFavNewsFragmentBinding
    private val mFavsViewModel by viewModels<FavsViewModel> {
        getArticleViewModelFactory()
    }
    private lateinit var mApplication: Application

    private val mMyArticleAdapter = MyArticleAdapter(object : ArticleClickListenerInterface {
        override fun onArticleClick(article: Article) {
            CustomTabsUtils.launchCustomTabs(context, article.articleUrl)
        }

        override fun liked(likeButton: LikeButton?, article: Article) {
            //Do nothing because only removals can happen here
        }

        override fun unLiked(likeButton: LikeButton?, article: Article) {
            mFavsViewModel.deleteArticleFromFavs(article)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.my_fav_news_fragment, container, false)

        setHasOptionsMenu(true)
        binding.lifecycleOwner = this

        val gridLayoutManager = StaggeredGridLayoutManager(context?.resources!!.getInteger(R.integer.column_article_count),
                StaggeredGridLayoutManager.VERTICAL)

        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = mMyArticleAdapter

        mApplication = this.activity?.application!!

        FragmentUtils.observeForArticlesFromDb(viewLifecycleOwner, mFavsViewModel.favArticlesFromDb,
                mMyArticleAdapter)

        mFavsViewModel.favArticlesFromDb.observe(viewLifecycleOwner, { articleList ->
            if (articleList.isNullOrEmpty()) {
                //Show the empty layout and hide the clear button
                binding.layoutEmptyFavs.visibility = View.VISIBLE
                binding.fabClearFavs.visibility = View.GONE
            } else {
                //Hide the empty layout and show the clear button
                binding.layoutEmptyFavs.visibility = View.GONE
                binding.fabClearFavs.visibility = View.VISIBLE

                //Show the articles
                mMyArticleAdapter.submitList(articleList)

                //Make sure all of them are checked
                for (article in articleList) {
                    article.isLiked = true
                }
            }
        })

        mFavsViewModel.showSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) { // Observed state is true and all data is cleared.
                Snackbar.make(activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_LONG
                ).show()
                mFavsViewModel.doneShowingSnackbar()
            }
        })

        binding.favFragmentViewModel = mFavsViewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragment_search -> {
                findNavController().navigate(
                        FavsFragmentDirections.actionFavsFragmentToFragmentSearchDest(PrefUtils.categoryGeneral(context!!)!!)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
