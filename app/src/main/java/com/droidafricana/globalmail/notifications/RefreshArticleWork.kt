package com.droidafricana.globalmail.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.droidafricana.globalmail.domain.Article
import com.droidafricana.globalmail.service.model.asDomainModel
import com.droidafricana.globalmail.service.network.ArticleApi
import com.droidafricana.globalmail.utils.Constants
import com.droidafricana.globalmail.utils.NotificationUtils
import com.droidafricana.globalmail.utils.PrefUtils
import retrofit2.HttpException

class RefreshArticleWork(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {
    companion object {
        const val REFRESH_ARTICLE_WORK_NAME = "RefreshArticleWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val articleListDeferred = ArticleApi.apiService.getArticleListAsync(
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

//    class Factory : WorkerFactory() {
//        override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {
//            return RefreshArticleWork(appContext, workerParameters)
//        }
//    }
}