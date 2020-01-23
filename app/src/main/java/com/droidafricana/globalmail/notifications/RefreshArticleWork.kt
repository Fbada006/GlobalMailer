package com.droidafricana.globalmail.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.droidafricana.globalmail.Constants
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.asDomainModel
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.NotificationUtils
import com.droidafricana.globalmail.utils.PrefUtils
import retrofit2.HttpException

class RefreshArticleWork(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {
    private val TAG = RefreshArticleWork::class.java.simpleName

    companion object {
        const val WORK_NAME = "RefreshArticleWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val articleListDeferred = ArticleApi.retrofitService.getArticleListAsync(
                    PrefUtils.getEndpoint(applicationContext), null,
                    PrefUtils.categoryGeneral(applicationContext), PrefUtils.getCountry(applicationContext),
                    Constants.PAGE_SIZE, PrefUtils.getSortByParam(applicationContext),
                    Constants.TEST_API_KEY).await()

            val articleList: List<Article> = articleListDeferred.asDomainModel()

            NotificationUtils.executeArticleNotificationTask(applicationContext,
                    NotificationUtils.ACTION_ISSUE_ARTICLE_NOTIFICATION, articleList[0])
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}