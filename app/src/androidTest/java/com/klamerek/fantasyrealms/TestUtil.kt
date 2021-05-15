package com.klamerek.fantasyrealms

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStream
import java.util.*


fun setLocale(language: String, country: String) {
    val locale = Locale(language, country)
    Locale.setDefault(locale)
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val config = Configuration(context.resources.configuration)
    Locale.setDefault(locale)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

fun getBitmapFromTestAssets(fileName: String?): Bitmap {
    val testContext: Context = InstrumentationRegistry.getInstrumentation().context
    val assetManager: AssetManager = testContext.assets
    val testInput: InputStream = assetManager.open(fileName!!)
    return BitmapFactory.decodeStream(testInput)
}
