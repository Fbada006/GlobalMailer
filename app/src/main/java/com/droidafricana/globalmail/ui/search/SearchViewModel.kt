package com.droidafricana.globalmail.ui.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.asDomainModel
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.Constants
import com.droidafricana.globalmail.utils.PrefUtils.getCountry
import com.droidafricana.globalmail.utils.PrefUtils.getEndpoint
import com.droidafricana.globalmail.utils.PrefUtils.getSortByParam
import kotlinx.coroutines.launch

enum class ArticleApiStatus { LOADING, ERROR, DONE }

class SearchViewModel internal constructor(application: Application,
                                           articleCategory: String?, queryParam: String?) :
        ViewModel() {

    private val _articlesObservable = MutableLiveData<List<Article>>()

    val liveArticlesObservable: LiveData<List<Article>>
        get() = _articlesObservable

    // The internal MutableLiveData String that stores the most recent response status
    private val _status = MutableLiveData<ArticleApiStatus>()

    // The external immutable LiveData for the status String
    val articleLoadingStatus: LiveData<ArticleApiStatus>
        get() = _status

    init {
        getLiveArticles(application, articleCategory, queryParam)
    }

    fun getLiveArticles(context: Context?, articleCategory: String?, queryParam: String?) {
        viewModelScope.launch {
            val getArticlesDeferred = ArticleApi.apiService.getArticleListAsync(
                    getEndpoint(context), queryParam, articleCategory,
                    getCountry(context), Constants.PAGE_SIZE, getSortByParam(context),
                    Constants.TEST_API_KEY
            )
            try {
                _status.value = ArticleApiStatus.LOADING
                val articleResponse = getArticlesDeferred.await()
                _status.value = ArticleApiStatus.DONE

                //We get back a NetworkArticle object, which needs to be mapped as the domain Article object
                _articlesObservable.value = articleResponse.asDomainModel()

            } catch (e: Exception) {
                _status.value = ArticleApiStatus.ERROR
            }
        }
    }
}
