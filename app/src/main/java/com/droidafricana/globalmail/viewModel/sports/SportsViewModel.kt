package com.droidafricana.globalmail.viewModel.sports

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SportsViewModel internal constructor(application: Application, articleCategory: String?) :
        ViewModel() {

    private val _articlesObservable = MutableLiveData<List<Article>>()

    //Database instance for use with the repository
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(application)

    //Repository for getting all data from the DB
    private val articleRepository = ArticleRepository(application, articleCategory, articleDatabase)
            .getInstance()

    init {
        refreshSportsDataInDb(application)
    }

    fun refreshSportsDataInDb(application: Application) {
        //Check for network connectivity before refreshing the generalArticles
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            viewModelScope.launch {
                articleRepository.refreshSportsArticleDatabaseFromNetwork()
            }
        }
    }

    val sportsArticlesFromDb = articleRepository.sportsArticles

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

class SportsArticleViewModelFactory(private val mApplication: Application,
                                    private val mArticleCategory: String?) :
        ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SportsViewModel::class.java)) {
            return SportsViewModel(mApplication, mArticleCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}