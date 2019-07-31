package com.droidafricana.globalmail.chromeCustomTabs.receiver

import android.content.*

class CustomTabsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val uri = intent.data
        if (uri != null) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val label = "Article URL"
            val clipData = ClipData.newUri(null, label, uri)
            assert(clipboardManager != null)
            clipboardManager.primaryClip = clipData

        }
    }
}
