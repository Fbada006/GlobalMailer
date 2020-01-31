package com.droidafricana.globalmail.database.business

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "business_articles_table")
data class BusinessDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<BusinessDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface BusinessArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from business_articles_table")
    fun getAllBusinessArticles(): LiveData<List<BusinessDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of BusinessDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBusinessArticles(vararg articles: BusinessDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM business_articles_table")
    suspend fun clearBusinessTable()
}