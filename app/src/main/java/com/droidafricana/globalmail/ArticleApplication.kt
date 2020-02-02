package com.droidafricana.globalmail

import android.app.Application
import androidx.work.*
import com.droidafricana.globalmail.notifications.RefreshArticleWork
import com.droidafricana.globalmail.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ArticleApplication : Application() {
    private val TAG: String = ArticleApplication::class.java.name

    val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    //  use a Builder to define a repeatingRequest variable to handle scheduling work.
    private fun setupRecurringWork() {
//        val workManagerConfig = Configuration.Builder()
//                .setWorkerFactory(RefreshArticleWork.Factory())
//                .build()
//
//        WorkManager.initialize(this, workManagerConfig)

        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

        // Add the constraints to the repeatingRequest definition.
        val interval = PrefUtils.notificationInterval(applicationContext).toLong()

        val repeatingRequest =
                PeriodicWorkRequestBuilder<RefreshArticleWork>(interval, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build()

        // get an instance of WorkManager and launch call enqueuePeriodicWork() to schedule the work.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                RefreshArticleWork.REFRESH_ARTICLE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest)
    }

    override fun onCreate() {
        super.onCreate()

        delayedInit()
    }
}