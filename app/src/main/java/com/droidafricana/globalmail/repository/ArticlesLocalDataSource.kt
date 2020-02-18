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

class ArticlesLocalDataSource(context: Context) : IArticlesDataSource {
    private val articleDatabase = ArticleDatabase.getDatabaseInstance(context)

    override suspend fun getArticles(context: Context, query: String?, category: String?): ArticleResponse {
        return ArticleResponse()
    }

    override suspend fun saveGeneralArticles(articleResponse: ArticleResponse) {
        articleDatabase.generalArticleDao.clearGeneralTable()
        articleDatabase.generalArticleDao.insertAllGeneralArticles(*articleResponse.asGeneralDatabaseModel())
    }

    override suspend fun saveSportsArticles(articleResponse: ArticleResponse) {
        articleDatabase.sportsArticleDao.clearSportsTable()
        articleDatabase.sportsArticleDao.insertAllSportsArticles(*articleResponse.asSportsDatabaseModel())
    }

    override suspend fun saveTechArticles(articleResponse: ArticleResponse) {
        articleDatabase.technologyArticleDao.clearTechnologyTable()
        articleDatabase.technologyArticleDao.insertAllTechnologyArticles(*articleResponse.asTechDatabaseModel())
    }

    override suspend fun saveEntArticles(articleResponse: ArticleResponse) {
        articleDatabase.entArticleDao.clearEntTable()
        articleDatabase.entArticleDao.insertAllEntArticles(*articleResponse.asEntDatabaseModel())
    }

    override suspend fun saveHealthArticles(articleResponse: ArticleResponse) {
        articleDatabase.healthArticleDao.clearHealthTable()
        articleDatabase.healthArticleDao.insertAllHealthArticles(*articleResponse.asHealthDatabaseModel())
    }

    override suspend fun saveScienceArticles(articleResponse: ArticleResponse) {
        articleDatabase.scienceArticleDao.clearScienceTable()
        articleDatabase.scienceArticleDao.insertAllScienceArticles(*articleResponse.asScienceDatabaseModel())
    }

    override suspend fun saveBusinessArticles(articleResponse: ArticleResponse) {
        articleDatabase.busArticleDao.clearBusinessTable()
        articleDatabase.busArticleDao.insertAllBusinessArticles(*articleResponse.asBusinessDatabaseModel())
    }

    override fun getGeneralArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.generalArticleDao.getAllGeneralArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getSportsArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.sportsArticleDao.getAllSportsArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getTechArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.technologyArticleDao.getAllTechnologyArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getEntArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.entArticleDao.getAllEntArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getHealthArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.healthArticleDao.getAllHealthArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getScienceArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.scienceArticleDao.getAllScienceArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getBusinessArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.busArticleDao.getAllBusinessArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override fun getFavouriteArticles(): LiveData<List<Article>>? =
            Transformations.map(articleDatabase.favsArticleDao.getAllFavArticles()) {
                it.mappedAsDomainArticleModel()
            }

    override suspend fun insertArticleToFav(article: Article) {
        article.isLiked = true
        articleDatabase.favsArticleDao.insertFavArticle(article.asFavsDatabaseModel())
    }

    override suspend fun deleteArticleInFav(article: Article) {
        article.isLiked = false
        articleDatabase.favsArticleDao.deleteArticle(article.asFavsDatabaseModel())
    }

    override suspend fun deleteAllArticlesInFav() = articleDatabase.favsArticleDao.clearFavsTable()
}