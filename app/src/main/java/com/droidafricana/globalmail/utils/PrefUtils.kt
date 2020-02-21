package com.droidafricana.globalmail.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.droidafricana.globalmail.R

/**A helper object for the shared preferences*/
object PrefUtils {

    /**Sort the articles value*/
    fun getSortByParam(context: Context?): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
                context?.getString(R.string.pref_sort_by_key),
                context?.getString(R.string.pref_sort_by_default))
    }

    /**Get the endpoint*/
    fun getEndpoint(context: Context?): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
                context?.getString(R.string.pref_endpoint_key),
                context?.getString(R.string.pref_endpoint_default))
    }

    /**Get country of news to show*/
    fun getCountry(context: Context?): String? {
        if (isEndPointEverything(context!!)) return null

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
                context.getString(R.string.pref_country_news_to_display_key),
                context.getString(R.string.pref_country_news_to_display_default))
    }

    private fun isEndPointEverything(context: Context): Boolean {
        val endPoint = getEndpoint(context)
        return endPoint == "everything"
    }

    /**Simple helper method for the general category*/
    fun categoryGeneral(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_general_value)
    }

    /**Simple helper method for the sports category*/
    fun categorySports(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_sports_value)
    }

    /**Simple helper method for the ent category*/
    fun categoryEnt(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_entertainment_value)
    }

    /**Simple helper method for the tech category*/
    fun categoryTech(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_technology_value)
    }

    /**Simple helper method for the business category*/
    fun categoryBus(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_business_value)
    }

    /**Simple helper method for the health category*/
    fun categoryHealth(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_health_value)
    }

    /**Simple helper method for the science category*/
    fun categoryScience(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_science_value)
    }

    /**Check if the notifications are enables*/
    fun areNotificationsEnabled(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(
                        context.getString(R.string.pref_notifications_key),
                        context.resources.getBoolean(R.bool.notification_default_value))
    }

    /**If notifications are on, get the proper interval*/
    fun notificationInterval(context: Context): Int {
        val string = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(
                        context.getString(R.string.pref_notification_interval_key),
                        context.getString(R.string.pref_default_notification_interval))!!
        return Integer.valueOf(string)
    }
}
