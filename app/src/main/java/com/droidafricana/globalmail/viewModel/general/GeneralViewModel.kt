package com.droidafricana.globalmail.viewModel.general

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class GeneralArticleApiStatus { LOADING, ERROR, DONE }

class GeneralViewModel internal constructor(application: Application, articleCategory: String?) :
        ViewModel() {

    // The internal MutableLiveData String that stores the most recent response status
    private val _status = MutableLiveData<GeneralArticleApiStatus>()

    // The external immutable LiveData for the status String
    val articleLoadingStatus: LiveData<GeneralArticleApiStatus>
        get() = _status

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
        refreshGeneralDataInDb(application)
    }

    fun refreshGeneralDataInDb(application: Application) {
        //Check for network connectivity before refreshing the generalArticles
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true

        if (isConnected) {
            articleCoroutineScope.launch {
                articleRepository.refreshGeneralArticleDatabaseFromNetwork()
            }
        }
    }

    //This is a LiveData containing the articleList from the Database
    val generalArticlesFromDb = articleRepository.generalArticles

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

class GeneralViewModelFactory(private val mApplication: Application,
                              private val mArticleCategory: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralViewModel::class.java)) {
            return GeneralViewModel(mApplication, mArticleCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
