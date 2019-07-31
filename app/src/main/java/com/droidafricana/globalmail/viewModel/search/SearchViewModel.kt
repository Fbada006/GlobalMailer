package com.droidafricana.globalmail.viewModel.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.droidafricana.globalmail.Constants
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.asDomainModel
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.PrefUtils.getCountry
import com.droidafricana.globalmail.utils.PrefUtils.getEndpoint
import com.droidafricana.globalmail.utils.PrefUtils.getSortByParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ArticleApiStatus { LOADING, ERROR, DONE }

class SearchViewModel internal constructor(application: Application,
                                           articleCategory: String?, queryParam: String?) :
        ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val articleCoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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
        articleCoroutineScope.launch {
            val getArticlesDeferred = ArticleApi.retrofitService.getArticleListAsync(
                    getEndpoint(context), queryParam, articleCategory,
                    getCountry(context), Constants.PAGE_SIZE, getSortByParam(context),
                    Constants.API_KEY
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

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

class SearchViewModelFactory(private val mApplication: Application,
                             private val mArticleCategory: String?,
                             private val queryParam: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(mApplication, mArticleCategory, queryParam) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
