package com.droidafricana.globalmail.viewModel.favs

import android.app.Application
import android.util.Log
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

val TAG = "FavsViewModel"

class FavsViewModel internal constructor(application: Application) :
        ViewModel() {

    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val articleCoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //Database instance for use with the repository
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(application)

    //Repository for getting all data from the DB
    private val articleRepository = ArticleRepository(application, null, articleDatabase)
            .getInstance()

    //This is a LiveData containing the articleList from the Database
    val favArticlesFromDb = articleRepository.favouriteArticles

    fun insertArticleIntoFavs(article: Article?) {
        articleCoroutineScope.launch {
            articleRepository.insertArticleToFav(article!!)
            Log.e(TAG, "Success writing! ")
        }
    }

    fun deleteArticleFromFavs(article: Article?) {
        articleCoroutineScope.launch {
            articleRepository.deleteArticleInFav(article!!)
            Log.e(TAG, "Success deleting! ")
        }
    }

    fun clearAllFavs() {
        articleCoroutineScope.launch {
            articleRepository.deleteAllArticlesInFav()
        }

        //Show a snackBar with the appropriate message
        _showSnackBarEvent.value = true
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class FavsViewModelFactory(private val mApplication: Application)
        : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavsViewModel::class.java)) {
                return FavsViewModel(mApplication) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}