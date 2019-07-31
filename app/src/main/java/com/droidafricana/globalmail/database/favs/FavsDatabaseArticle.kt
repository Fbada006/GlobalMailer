package com.droidafricana.globalmail.database.favs

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droidafricana.globalmail.domain.Article

@Entity(tableName = "favs_articles_table")
data class FavsDatabaseArticle(
        val articleTitle: String,
        val articleDescription: String?,
        @PrimaryKey //Because every article must have a unique URL
        val articleUrl: String,
        val articleUrlToImage: String?,
        val articleContent: String?
)

fun List<FavsDatabaseArticle>.mappedAsDomainArticleModel(): List<Article> {
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
interface FavsArticleDatabaseDao {
    //Get all the generalArticles
    @Query("select * from favs_articles_table")
    fun getAllFavArticles(): LiveData<List<FavsDatabaseArticle>>

    //Add generalArticles to the database list with an argument of a variable number of FavsDatabaseArticle objects
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavArticle(article: FavsDatabaseArticle)

    //Delete a single item
    @Delete
    fun deleteArticle(articleTitle: FavsDatabaseArticle)

    //Empty the database if we are getting new generalArticles
    @Query("DELETE FROM favs_articles_table")
    fun clearFavsTable()
}