package com.klamerek.fantasyrealms.util

import android.content.Context
import android.content.SharedPreferences
import com.klamerek.fantasyrealms.R

object Preferences {

    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
    }

    fun getDisplayCardNumber(context: Context): Boolean {
        val sharedPref = sharedPreferences(context)
        return sharedPref.getBoolean(context.getString(R.string.display_card_number), true)
    }

    fun saveDisplayCardNumberInPreferences(context: Context, display: Boolean) {
        val sharedPref = sharedPreferences(context)
        with(sharedPref.edit()) {
            putBoolean(context.getString(R.string.display_card_number), display)
            apply()
        }
    }

}
