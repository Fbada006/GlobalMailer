package com.droidafricana.globalmail.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.droidafricana.globalmail.MainActivity
import com.droidafricana.globalmail.R
import com.droidafricana.globalmail.domain.Article

/**
 * Utility class for creating article notifications
 */
object NotificationUtils {

    /**
     * This notification ID can be used to access our notification after we've displayed it.
     */
    private const val ARTICLE_REMINDER_NOTIFICATION_ID = 1138
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private const val ARTICLE_REMINDER_PENDING_INTENT_ID = 3417
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private const val ARTICLE_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel"
    private const val ACTION_IGNORE_ARTICLE_PENDING_INTENT_ID = 14

    private const val ACTION_DISMISS_NOTIFICATION = "dismiss-article-notification"
    const val ACTION_ISSUE_ARTICLE_NOTIFICATION = "new-article-notification"

    private fun clearAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    /*This method handles the creation of the channel, setting notification attribs
    * and sending the actual notification*/
    private fun sendArticleNotificationToUser(context: Context, article: Article) {
        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    ARTICLE_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }

        //Use builder pattern to create the notification
        val notificationBuilder = NotificationCompat.Builder(context, ARTICLE_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.resources, R.drawable.global_mail_final_logo))
                .setSmallIcon(R.drawable.global_mail_final_logo)
                .setContentTitle(article.articleTitle)
                .setContentText(article.articleDescription)
                .setStyle(NotificationCompat.BigTextStyle().bigText(
                        article.articleDescription))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                // .addAction(ignoreNotificationAction(context))
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }
        notificationManager.notify(ARTICLE_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    /* private fun ignoreNotificationAction(context: Context): Action {
         val ignoreReminderIntent = Intent(context, RefreshArticleWork::class.java)
         ignoreReminderIntent.action = ACTION_DISMISS_NOTIFICATION
         val ignoreReminderPendingIntent = PendingIntent.getService(
                 context,
                 ACTION_IGNORE_ARTICLE_PENDING_INTENT_ID,
                 ignoreReminderIntent,
                 PendingIntent.FLAG_UPDATE_CURRENT)
         return Action(R.drawable.ic_cancel,
                 context.getString(R.string.action_ignore_article_reminder_title),
                 ignoreReminderPendingIntent)
     }*/

    //Pending intent to launch activity
    private fun contentIntent(context: Context): PendingIntent {
        val startActivityIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context,
                ARTICLE_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**This method executes depending on the action
    * TODO: Needs some cleaning to set actions correctly*/
    fun executeArticleNotificationTask(context: Context, action: String, article: Article) {
        if (ACTION_DISMISS_NOTIFICATION == action) {
            clearAllNotifications(context)
        } else if (ACTION_ISSUE_ARTICLE_NOTIFICATION == action) {
            if (PrefUtils.areNotificationsEnabled(context)) {
                sendArticleNotificationToUser(context, article)
                vibrateDevice(context)
            }
        }
    }

    private fun vibrateDevice(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500,
                    VibrationEffect.DEFAULT_AMPLITUDE))
        }
        //For other devices running below API 26
        vibrator.vibrate(500)
    }
}