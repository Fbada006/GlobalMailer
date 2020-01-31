package com.droidafricana.globalmail.ui.business

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.launch

class BusinessViewModel internal constructor(application: Application, articleCategory: String?) :
        ViewModel() {

    //Database instance for use with the repository
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(application)

    //Repository for getting all data from the DB
    private val articleRepository = ArticleRepository(application, articleCategory, articleDatabase)
            .getInstance()

    init {
        refreshBusDataInDb(application)
    }

    fun refreshBusDataInDb(application: Application) {
        //Check for network connectivity before refreshing the generalArticles
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            viewModelScope.launch {
                articleRepository.refreshBusinessArticleDatabaseFromNetwork()
            }
        }
    }

    val busArticlesFromDb = articleRepository.businessArticles

    fun insertArticleIntoFavs(article: Article?) {
        viewModelScope.launch {
            articleRepository.insertArticleToFav(article!!)
        }
    }

    fun deleteArticleFromFavs(article: Article?) {
        viewModelScope.launch {
            articleRepository.deleteArticleInFav(article!!)
        }
    }

}
