package com.klamerek.fantasyrealms

import android.app.Application
import android.content.res.Resources
import androidx.annotation.StringRes

class App : Application() {
    companion object {
        lateinit var mResources: Resources
    }

    override fun onCreate() {
        super.onCreate()
        mResources = LocaleManager.updateContextWithPreferredLanguage(baseContext).resources
    }

}

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.mResources.getString(stringRes, *formatArgs)
    }

    fun getArray(arrayRes: Int): Array<out String> {
        return App.mResources.getStringArray(arrayRes)
    }
}
