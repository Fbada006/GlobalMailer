package com.droidafricana.globalmail.viewModel.health

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.launch

class HealthViewModel internal constructor(application: Application, articleCategory: String?) :
        ViewModel() {

    //Database instance for use with the repository
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(application)

    //Repository for getting all data from the DB
    private val articleRepository = ArticleRepository(application, articleCategory, articleDatabase)
            .getInstance()

    init {
        refreshHealthDataInDb(application)
    }

    fun refreshHealthDataInDb(application: Application) {
        //Check for network connectivity before refreshing the generalArticles
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            viewModelScope.launch {
                articleRepository.refreshHealthArticleDatabaseFromNetwork()
            }
        }
    }

    val healthArticlesFromDb = articleRepository.healthArticles

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

class HealthViewModelFactory(private val mApplication: Application,
                             private val mArticleCategory: String?) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            return HealthViewModel(mApplication, mArticleCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
