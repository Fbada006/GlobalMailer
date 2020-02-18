package com.droidafricana.globalmail.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.ArticleResponse
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.Constants
import com.droidafricana.globalmail.utils.PrefUtils

/**
 *Gets the articles from the network
 */
object ArticlesRemoteDataSource : IArticlesDataSource {
    override suspend fun getArticles(context: Context,
                                     query: String?,
                                     category: String?): ArticleResponse {
        return ArticleApi.apiService.getArticleListAsync(PrefUtils.getEndpoint(context), query,
                category, PrefUtils.getCountry(context), Constants.PAGE_SIZE,
                PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()
    }

    override suspend fun saveGeneralArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveSportsArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveTechArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveEntArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveHealthArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveScienceArticles(articleResponse: ArticleResponse) {}

    override suspend fun saveBusinessArticles(articleResponse: ArticleResponse) {}

    override fun getGeneralArticles(): LiveData<List<Article>>? = null

    override fun getSportsArticles(): LiveData<List<Article>>? = null

    override fun getTechArticles(): LiveData<List<Article>>? = null

    override fun getEntArticles(): LiveData<List<Article>>? = null

    override fun getHealthArticles(): LiveData<List<Article>>? = null

    override fun getScienceArticles(): LiveData<List<Article>>? = null

    override fun getBusinessArticles(): LiveData<List<Article>>? = null

    override fun getFavouriteArticles(): LiveData<List<Article>>? = null

    override suspend fun insertArticleToFav(article: Article) {}

    override suspend fun deleteArticleInFav(article: Article) {}

    override suspend fun deleteAllArticlesInFav() {}
}