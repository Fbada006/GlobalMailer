package com.droidafricana.globalmail.service.model

import com.droidafricana.globalmail.database.business.BusinessDatabaseArticle
import com.droidafricana.globalmail.database.entertainment.EntDatabaseArticle
import com.droidafricana.globalmail.database.favs.FavsDatabaseArticle
import com.droidafricana.globalmail.database.general.GeneralDatabaseArticle
import com.droidafricana.globalmail.database.health.HealthDatabaseArticle
import com.droidafricana.globalmail.database.science.ScienceDatabaseArticle
import com.droidafricana.globalmail.database.sports.SportsDatabaseArticle
import com.droidafricana.globalmail.database.technology.TechnologyDatabaseArticle
import com.droidafricana.globalmail.domain.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class NetworkArticle(
        @Json(name = "title")
        val articleTitle: String,
        @Json(name = "description")
        val articleDescription: String?,
        @Json(name = "url")
        val articleUrl: String,
        @Json(name = "urlToImage")
        val articleUrlToImage: String?,
        @Json(name = "content")
        val articleContent: String?)

@JsonClass(generateAdapter = true)
data class ArticleResponse(
        @Json(name = "articles")
        val articleList: List<NetworkArticle> = ArrayList()
)

fun ArticleResponse.asGeneralDatabaseModel(): Array<GeneralDatabaseArticle> {
    return articleList.map {
        GeneralDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asSportsDatabaseModel(): Array<SportsDatabaseArticle> {
    return articleList.map {
        SportsDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asEntDatabaseModel(): Array<EntDatabaseArticle> {
    return articleList.map {
        EntDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asTechDatabaseModel(): Array<TechnologyDatabaseArticle> {
    return articleList.map {
        TechnologyDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asBusinessDatabaseModel(): Array<BusinessDatabaseArticle> {
    return articleList.map {
        BusinessDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asHealthDatabaseModel(): Array<HealthDatabaseArticle> {
    return articleList.map {
        HealthDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asScienceDatabaseModel(): Array<ScienceDatabaseArticle> {
    return articleList.map {
        ScienceDatabaseArticle(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent)
    }.toTypedArray()
}

fun ArticleResponse.asDomainModel(): List<Article> {
    return articleList.map {
        Article(
                articleTitle = it.articleTitle,
                articleDescription = it.articleDescription,
                articleUrl = it.articleUrl,
                articleUrlToImage = it.articleUrlToImage,
                articleContent = it.articleContent,
                isLiked = false)
    }
}

//This maps an article into FavsDatabaseArticle array. Adding to the database
//involves taking an Article object instead of one already from the network
fun Article.asFavsDatabaseModel() = FavsDatabaseArticle(
        articleTitle = articleTitle,
        articleDescription = articleDescription,
        articleUrl = articleUrl,
        articleUrlToImage = articleUrlToImage,
        articleContent = articleContent)


