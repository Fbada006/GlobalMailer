package com.droidafricana.globalmail.database.science

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "science_articles_table")
data class ScienceDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<ScienceDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface ScienceArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from science_articles_table")
    fun getAllScienceArticles(): LiveData<List<ScienceDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of ScienceDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllScienceArticles(vararg articles: ScienceDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM science_articles_table")
    suspend fun clearScienceTable()
}