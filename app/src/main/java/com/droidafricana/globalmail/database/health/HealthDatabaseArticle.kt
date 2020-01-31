package com.droidafricana.globalmail.database.health

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "health_articles_table")
data class HealthDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<HealthDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface HealthArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from health_articles_table")
    fun getAllHealthArticles(): LiveData<List<HealthDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of HealthDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHealthArticles(vararg articles: HealthDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM health_articles_table")
    suspend fun clearHealthTable()
}