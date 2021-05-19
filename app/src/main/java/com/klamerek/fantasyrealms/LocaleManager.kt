package com.klamerek.fantasyrealms

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

/**
 * Utility to update language and save it in application preferences
 */
object LocaleManager {

    fun updateContextWithPreferredLanguage(context: Context): Context {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val country = sharedPref.getString(context.getString(R.string.saved_country), Locale.getDefault().country)
        val language = sharedPref.getString(context.getString(R.string.saved_language), Locale.getDefault().language)
        return updateContextWithPreferredLanguage(context, language!!, country!!)
    }

    fun updateContextWithPreferredLanguage(context: Context, language: String, country: String): Context {
        val locale = Locale(language, country)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val configuration = Configuration(res.configuration)
        configuration.setLocale(locale)
        val result = context.createConfigurationContext(configuration)
        App.mResources = result.resources
        return result
    }

    fun saveLanguageInPreferences(context: Context, language: String, country: String) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(context.getString(R.string.saved_language), language)
            putString(context.getString(R.string.saved_country), country)
            apply()
        }
    }

}
