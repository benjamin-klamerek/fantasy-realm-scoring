package com.klamerek.fantasyrealms

import android.app.Application
import androidx.annotation.StringRes

class App : Application() {
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}