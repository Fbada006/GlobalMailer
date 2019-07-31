package com.droidafricana.globalmail.utils

import android.content.Context
import android.preference.PreferenceManager
import com.droidafricana.globalmail.R

object PrefUtils {

    fun getSortByParam(context: Context?): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
                context?.getString(R.string.pref_sort_by_key),
                context?.getString(R.string.pref_sort_by_default))
    }

    fun getEndpoint(context: Context?): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
                context?.getString(R.string.pref_endpoint_key),
                context?.getString(R.string.pref_endpoint_default))
    }

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

    /*Simple helper methods for the category*/
    fun categoryGeneral(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_general_value)
    }

    fun categorySports(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_sports_value)
    }

    fun categoryEnt(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_entertainment_value)
    }

    fun categoryTech(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_technology_value)
    }

    fun categoryBus(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_business_value)
    }

    fun categoryHealth(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_health_value)
    }

    fun categoryScience(context: Context): String? {
        return if (isEndPointEverything(context)) null else context.getString(R.string.category_science_value)
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(
                        context.getString(R.string.pref_notifications_key),
                        context.resources.getBoolean(R.bool.notification_default_value))
    }

    fun notificationInterval(context: Context): Int {
        val string = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(
                        context.getString(R.string.pref_notification_interval_key),
                        context.getString(R.string.pref_default_notification_interval))!!
        return Integer.valueOf(string)
    }
}
