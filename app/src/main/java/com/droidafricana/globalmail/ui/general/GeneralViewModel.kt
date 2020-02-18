package com.droidafricana.globalmail.ui.general

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.launch

class GeneralViewModel constructor(application: Application,
                                   private val articleRepository: ArticleRepository) :
        ViewModel() {
    private val TAG = "GeneralViewModel"

    init {
        refreshGeneralDataInDb(application)
        Log.e(TAG, "General created: ")
    }

    fun refreshGeneralDataInDb(application: Application) {
        //Check for network connectivity before refreshing the generalArticles
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            Log.e(TAG, "General refresh called!: ")
            viewModelScope.launch {
                articleRepository.refreshGeneralArticleDatabaseFromNetwork()
            }
        }
    }

    //This is a LiveData containing the articleList from the Database
    val generalArticlesFromDb = articleRepository.generalArticles

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
