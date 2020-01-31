package com.droidafricana.globalmail.database.entertainment

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "ent_articles_table")
data class EntDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<EntDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface EntertainmentArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from ent_articles_table")
    fun getAllEntArticles(): LiveData<List<EntDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of EntDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEntArticles(vararg articles: EntDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM ent_articles_table")
    suspend fun clearEntTable()
}