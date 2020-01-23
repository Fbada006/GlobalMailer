package com.droidafricana.globalmail.viewModel.favs

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
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

    //Database instance for use with the repository
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(application)

    //Repository for getting all data from the DB
    private val articleRepository = ArticleRepository(application, null, articleDatabase)
            .getInstance()

    //This is a LiveData containing the articleList from the Database
    val favArticlesFromDb = articleRepository.favouriteArticles

    fun insertArticleIntoFavs(article: Article?) {
        viewModelScope.launch {
            articleRepository.insertArticleToFav(article!!)
            Log.e(TAG, "Success writing! ")
        }
    }

    fun deleteArticleFromFavs(article: Article?) {
        viewModelScope.launch {
            articleRepository.deleteArticleInFav(article!!)
            Log.e(TAG, "Success deleting! ")
        }
    }

    fun clearAllFavs() {
        viewModelScope.launch {
            articleRepository.deleteAllArticlesInFav()
        }

        //Show a snackBar with the appropriate message
        _showSnackBarEvent.value = true
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