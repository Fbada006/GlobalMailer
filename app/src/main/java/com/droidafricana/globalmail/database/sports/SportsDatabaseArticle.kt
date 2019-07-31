package com.droidafricana.globalmail.database.sports

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "sports_articles_table")
data class SportsDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<SportsDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface SportsArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from sports_articles_table")
    fun getAllSportsArticles(): LiveData<List<SportsDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of SportsDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSportsArticles(vararg articles: SportsDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM sports_articles_table")
    fun clearSportsTable()
}