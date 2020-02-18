package com.droidafricana.globalmail.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.ArticleResponse

interface IArticlesDataSource {
    suspend fun getArticles(context: Context,
                            query: String?,
                            category: String?): ArticleResponse

    suspend fun saveGeneralArticles(articleResponse: ArticleResponse)

    suspend fun saveSportsArticles(articleResponse: ArticleResponse)

    suspend fun saveTechArticles(articleResponse: ArticleResponse)

    suspend fun saveEntArticles(articleResponse: ArticleResponse)

    suspend fun saveHealthArticles(articleResponse: ArticleResponse)

    suspend fun saveScienceArticles(articleResponse: ArticleResponse)

    suspend fun saveBusinessArticles(articleResponse: ArticleResponse)

    fun getGeneralArticles(): LiveData<List<Article>>?

    fun getSportsArticles(): LiveData<List<Article>>?

    fun getTechArticles(): LiveData<List<Article>>?

    fun getEntArticles(): LiveData<List<Article>>?

    fun getHealthArticles(): LiveData<List<Article>>?

    fun getScienceArticles(): LiveData<List<Article>>?

    fun getBusinessArticles(): LiveData<List<Article>>?

    fun getFavouriteArticles(): LiveData<List<Article>>?

    suspend fun insertArticleToFav(article: Article)

    suspend fun deleteArticleInFav(article: Article)

    suspend fun deleteAllArticlesInFav()
}