package com.droidafricana.globalmail.viewModel.general

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
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

class GeneralViewModelFactory(private val mApplication: Application,
                              private val mArticleCategory: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralViewModel::class.java)) {
            return GeneralViewModel(mApplication, mArticleCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
