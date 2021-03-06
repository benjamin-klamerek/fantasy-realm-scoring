package com.klamerek.fantasyrealms.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.klamerek.fantasyrealms.App
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings
import java.util.*

data class Language(
    val id: String, val labelKey: Int,
    val countryCode: String, val language: String
) {
    fun label() = Strings.get(labelKey)

    override fun toString(): String {
        return label()
    }
}

/**
 * Utility to update language and save it in application preferences
 */
object LocaleManager {

    val english = Language("ENGLISH_ID", R.string.language_label_english, "en", "EN")
    val french = Language("FRENCH_ID", R.string.language_label_french, "fr", "FR")

    val languages = listOf(english, french)
    private val languagesById = languages.map { it.id to it }.toMap()

    fun getLanguage(context: Context): Language {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val key = sharedPref.getString(context.getString(R.string.saved_language), "")
        return languagesById[key] ?: languages.firstOrNull { it.language == Locale.getDefault().language } ?: english
    }

    fun updateContextWithPreferredLanguage(context: Context): Context {
        return updateContextWithPreferredLanguage(context, getLanguage(context))
    }

    fun updateContextWithPreferredLanguage(context: Context, language: Language): Context {
        val locale = Locale(language.language, language.countryCode)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val configuration = Configuration(res.configuration)
        configuration.setLocale(locale)
        val result = context.createConfigurationContext(configuration)
        App.mResources = result.resources
        return result
    }

    fun saveLanguageInPreferences(context: Context, language: Language) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(context.getString(R.string.saved_language), language.id)
            apply()
        }
    }

}
