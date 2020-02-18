package com.droidafricana.globalmail.ui.favs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.repository.ArticleRepository
import kotlinx.coroutines.launch

class FavsViewModel internal constructor(private val articleRepository: ArticleRepository) :
        ViewModel() {

    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackbar() {
        _showSnackBarEvent.value = false
    }

    //This is a LiveData containing the articleList from the Database
    val favArticlesFromDb = articleRepository.favouriteArticles

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

    fun clearAllFavs() {
        viewModelScope.launch {
            articleRepository.deleteAllArticlesInFav()
        }

        //Show a snackBar with the appropriate message
        _showSnackBarEvent.value = true
    }
}