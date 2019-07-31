package com.droidafricana.globalmail.database.general

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "general_articles_table")
data class GeneralDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<GeneralDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface GeneralArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from general_articles_table")
    fun getAllGeneralArticles(): LiveData<List<GeneralDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of GeneralDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGeneralArticles(vararg articles: GeneralDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM general_articles_table")
    fun clearGeneralTable()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(generalDatabaseArticle: GeneralDatabaseArticle)
}