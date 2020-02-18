package com.droidafricana.globalmail.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.droidafricana.globalmail.domain.Article

/**
 * Responsible for writing and reading from the database. This controls everything
 */
class ArticleRepository(private val context: Context,
                        private val articleCategory: String?,
                        private val articlesRemoteDataSource: IArticlesDataSource,
                        private val articlesLocalDataSource: IArticlesDataSource) {

    /**Refresh the general database cache*/
    suspend fun refreshGeneralArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveGeneralArticles(response)

        } catch (e: Exception) {
        }
    }

    /** Articles to display for the user to view Room executes all queries on a separate thread.
    Observed LiveData will notify the observer when the data has changed.*/
    val generalArticles: LiveData<List<Article>> = articlesLocalDataSource.getGeneralArticles()!!

    /**Refresh the sports database cache*/
    suspend fun refreshSportsArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveSportsArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Sports Articles to display for the user to view*/
    val sportsArticles: LiveData<List<Article>> = articlesLocalDataSource.getSportsArticles()!!

    /**Refresh the ent database cache*/
    suspend fun refreshEntArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveEntArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Entertainment Articles to display for the user to view*/
    val entArticles: LiveData<List<Article>> = articlesLocalDataSource.getEntArticles()!!

    /**Refresh the tech database cache*/
    suspend fun refreshTechArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveTechArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Tech Articles to display for the user to view*/
    val techArticles: LiveData<List<Article>> = articlesLocalDataSource.getTechArticles()!!

    /**Refresh the business database cache*/
    suspend fun refreshBusinessArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveBusinessArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Business Articles to display for the user to view*/
    val businessArticles: LiveData<List<Article>> = articlesLocalDataSource.getBusinessArticles()!!

    /**Refresh the health database cache*/
    suspend fun refreshHealthArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveHealthArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Health Articles to display for the user to view*/
    val healthArticles: LiveData<List<Article>> = articlesLocalDataSource.getHealthArticles()!!

    /**Refresh the science database cache*/
    suspend fun refreshScienceArticleDatabaseFromNetwork() {
        try {
            val response =
                    articlesRemoteDataSource.getArticles(context, null, articleCategory)
            articlesLocalDataSource.saveScienceArticles(response)
        } catch (e: Exception) {
        }
    }

    /**Science Articles to display for the user to view*/
    val scienceArticles: LiveData<List<Article>> = articlesLocalDataSource.getScienceArticles()!!

    /**Insert a favorite into the favorite table*/
    suspend fun insertArticleToFav(article: Article) = articlesLocalDataSource.insertArticleToFav(article)

    /**Delete a favorite from the favorite table*/
    suspend fun deleteArticleInFav(article: Article) = articlesLocalDataSource.deleteArticleInFav(article)

    /**Clear the favorite table*/
    suspend fun deleteAllArticlesInFav() = articlesLocalDataSource.deleteAllArticlesInFav()

    /**Favourite Articles to display for the user to view*/
    val favouriteArticles: LiveData<List<Article>> = articlesLocalDataSource.getFavouriteArticles()!!
}