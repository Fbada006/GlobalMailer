package com.droidafricana.globalmail.database.technology

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "technology_articles_table")
data class TechnologyDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<TechnologyDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
    return map {
        Article(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent,
                isLiked = false
        )
    }
}

@Dao
interface TechnologyArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from technology_articles_table")
    fun getAllTechnologyArticles(): LiveData<List<TechnologyDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of TechnologyDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTechnologyArticles(vararg articles: TechnologyDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM technology_articles_table")
    suspend fun clearTechnologyTable()
}