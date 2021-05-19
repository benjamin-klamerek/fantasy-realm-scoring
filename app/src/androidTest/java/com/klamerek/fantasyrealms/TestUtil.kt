package com.klamerek.fantasyrealms

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStream

fun getBitmapFromTestAssets(fileName: String?): Bitmap {
    val testContext: Context = InstrumentationRegistry.getInstrumentation().context
    val assetManager: AssetManager = testContext.assets
    val testInput: InputStream = assetManager.open(fileName!!)
    return BitmapFactory.decodeStream(testInput)
}
