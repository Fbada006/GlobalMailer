package com.droidafricana.globalmail.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.droidafricana.globalmail.chromeCustomTabs.customTabs.CustomTabActivityHelper
import com.droidafricana.globalmail.chromeCustomTabs.receiver.CustomTabsBroadcastReceiver


object CustomTabsUtils {
    private const val COPY_LINK_REQUEST_CODE = 0
    private const val ANDROID_APP_SCHEME = "android-app://"
    private const val DISABLE_DOWNLOAD_BUTTON_EXTRA =
            "org.chromium.chrome.browser.customtabs.EXTRA_DISABLE_DOWNLOAD_BUTTON"

    /*Set up the chrome custom tabs in the adapter*/
    fun launchCustomTabs(context: Context?, url: String) {

        val customTabsBuilder = CustomTabsIntent.Builder()
        customTabsBuilder.setToolbarColor(ContextCompat.getColor(context!!, com.droidafricana.globalmail.R.color.colorPrimaryDark))

        //Set up the copy link to clipboard functionality
        val copyIntent = Intent(context, CustomTabsBroadcastReceiver::class.java)
        val label = context.getString(com.droidafricana.globalmail.R.string.customs_tabs_copy_link_to_clipboard)

        val copyPendingIntent = PendingIntent.getBroadcast(context,
                COPY_LINK_REQUEST_CODE, copyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        customTabsBuilder.addMenuItem(label, copyPendingIntent)

        //Add the default share behavior
        customTabsBuilder.addDefaultShareMenuItem()

        //Show title as well as the url on the action bar
        customTabsBuilder.setShowTitle(true)

        // Application exit animation, Chrome enter animation.
        customTabsBuilder.setStartAnimations(context, com.droidafricana.globalmail.R.anim.slide_in_right, com.droidafricana.globalmail.R.anim.slide_out_left)
        customTabsBuilder.setExitAnimations(context, com.droidafricana.globalmail.R.anim.slide_in_left, com.droidafricana.globalmail.R.anim.slide_out_right)

        val customTabsIntent = customTabsBuilder.build()

        //Let websites know that this app is the one sending it traffic
        customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse(ANDROID_APP_SCHEME + context.packageName))

        //Disable the download button for policy purposes
        customTabsIntent.intent.putExtra(DISABLE_DOWNLOAD_BUTTON_EXTRA, true)

        CustomTabActivityHelper.openCustomTab(context, customTabsIntent, Uri.parse(url)
        ) { context1, uri ->
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context1.startActivity(intent)
        }
    }
}
