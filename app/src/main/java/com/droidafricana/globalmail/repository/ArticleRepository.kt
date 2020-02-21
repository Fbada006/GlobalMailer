package com.droidafricana.globalmail.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.droidafricana.globalmail.database.ArticleDatabase
import com.droidafricana.globalmail.database.business.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.entertainment.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.favs.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.general.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.health.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.science.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.sports.mappedAsDomainArticleModel
import com.droidafricana.globalmail.database.technology.mappedAsDomainArticleModel
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.*
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.Constants
import com.droidafricana.globalmail.utils.PrefUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**Responsible for writing and reading from the database. This controls everything*/
class ArticleRepository(private val context: Context,
                        private val articleCategory: String?,
                        private val articleDatabase: ArticleDatabase) {

    private var sArticleRepository: ArticleRepository? = null

    /**Return an instance of the [ArticleRepository]*/
    @Synchronized
    fun getInstance(): ArticleRepository {
        if (sArticleRepository == null) {
            sArticleRepository = ArticleRepository(context, articleCategory, articleDatabase)
        }
        return sArticleRepository as ArticleRepository
    }

    /**Refresh the general database cache*/
    suspend fun refreshGeneralArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                //Clear the table first in preparation of new data
                articleDatabase.generalArticleDao.clearGeneralTable()

                //Make the API call
                //Always set the query to null because only the search fragment will use that argument
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()

                //Write the new data to the empty table
                articleDatabase.generalArticleDao.insertAllGeneralArticles(*articleList.asGeneralDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val generalArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.generalArticleDao.getAllGeneralArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the sports database cache*/
    suspend fun refreshSportsArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.sportsArticleDao.clearSportsTable()

                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()

                articleDatabase.sportsArticleDao.insertAllSportsArticles(*articleList.asSportsDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val sportsArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.sportsArticleDao.getAllSportsArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the ent database cache*/
    suspend fun refreshEntArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.entArticleDao.clearEntTable()
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()

                articleDatabase.entArticleDao.insertAllEntArticles(*articleList.asEntDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val entArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.entArticleDao.getAllEntArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the tech database cache*/
    suspend fun refreshTechArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.technologyArticleDao.clearTechnologyTable()
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()
                articleDatabase.technologyArticleDao.insertAllTechnologyArticles(*articleList.asTechDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val techArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.technologyArticleDao.getAllTechnologyArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the business database cache*/
    suspend fun refreshBusinessArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.busArticleDao.clearBusinessTable()
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()
                articleDatabase.busArticleDao.insertAllBusinessArticles(*articleList.asBusinessDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val businessArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.busArticleDao.getAllBusinessArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the health database cache*/
    suspend fun refreshHealthArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.healthArticleDao.clearHealthTable()
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()
                articleDatabase.healthArticleDao.insertAllHealthArticles(*articleList.asHealthDatabaseModel())
            } catch (e: Exception) {
            }

        }
    }

    /**Articles to display for the user to view*/
    val healthArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.healthArticleDao.getAllHealthArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Refresh the science database cache*/
    suspend fun refreshScienceArticleDatabaseFromNetwork() {
        withContext(Dispatchers.IO) {
            try {
                articleDatabase.scienceArticleDao.clearScienceTable()
                val articleList = ArticleApi.retrofitService.getArticleListAsync(
                        PrefUtils.getEndpoint(context), null,
                        articleCategory, PrefUtils.getCountry(context),
                        Constants.PAGE_SIZE, PrefUtils.getSortByParam(context), Constants.TEST_API_KEY).await()
                articleDatabase.scienceArticleDao.insertAllScienceArticles(*articleList.asScienceDatabaseModel())
            } catch (e: Exception) {
            }
        }
    }

    /**Articles to display for the user to view*/
    val scienceArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.scienceArticleDao.getAllScienceArticles()) {
                it.mappedAsDomainArticleModel()
            }

    /**Insert a favorite into the favorite table*/
    suspend fun insertArticleToFav(article: Article) {
        withContext(Dispatchers.IO) {
            article.isLiked = true
            articleDatabase.favsArticleDao.insertFavArticle(article.asFavsDatabaseModel())
        }
    }

    /**Delete a favorite into the favorite table*/
    suspend fun deleteArticleInFav(article: Article) {
        withContext(Dispatchers.IO) {
            article.isLiked = false
            articleDatabase.favsArticleDao.deleteArticle(article.asFavsDatabaseModel())
        }
    }

    /**Clear the favorite table*/
    suspend fun deleteAllArticlesInFav() {
        withContext(Dispatchers.IO) {
            articleDatabase.favsArticleDao.clearFavsTable()
        }
    }

    /**All articles from the favourite table*/
    val favouriteArticles: LiveData<List<Article>> =
            Transformations.map(articleDatabase.favsArticleDao.getAllFavArticles()) {
                it.mappedAsDomainArticleModel()
            }
}