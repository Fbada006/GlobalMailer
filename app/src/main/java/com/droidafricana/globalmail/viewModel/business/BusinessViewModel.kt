package com.droidafricana.globalmail.viewModel.business

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BusinessViewModel internal constructor(application: Application, articleCategory: String?) :
        ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val articleCoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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
            articleCoroutineScope.launch {
                articleRepository.refreshBusinessArticleDatabaseFromNetwork()
            }
        }
    }

    val busArticlesFromDb = articleRepository.businessArticles

    fun insertArticleIntoFavs(article: Article?) {
        articleCoroutineScope.launch {
            articleRepository.insertArticleToFav(article!!)
        }
    }

    fun deleteArticleFromFavs(article: Article?) {
        articleCoroutineScope.launch {
            articleRepository.deleteArticleInFav(article!!)
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

class BusinessViewModelFactory(private val mApplication: Application,
                               private val mArticleCategory: String?) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessViewModel::class.java)) {
            return BusinessViewModel(mApplication, mArticleCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
