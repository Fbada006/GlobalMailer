package com.droidafricana.globalmail

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.droidafricana.globalmail.repository.ArticleRepository
import com.droidafricana.globalmail.repository.ArticlesLocalDataSource
import com.droidafricana.globalmail.repository.ArticlesRemoteDataSource

/**
 * The sole purpose of this singleton is to store and provide dependencies*/
object ServiceLocator {

    @Volatile
    var articleRepository: ArticleRepository? = null
        @VisibleForTesting set

    fun providesArticleRepository(context: Context, articleCategory: String): ArticleRepository {
        synchronized(this) {
            return articleRepository ?: createArticleRepository(context, articleCategory)
        }
    }

    private fun createArticleRepository(context: Context, articleCategory: String): ArticleRepository {
        val articleRepo =
                ArticleRepository(context, articleCategory, ArticlesRemoteDataSource, ArticlesLocalDataSource(context))
        articleRepository = articleRepo
        return articleRepo
    }
}