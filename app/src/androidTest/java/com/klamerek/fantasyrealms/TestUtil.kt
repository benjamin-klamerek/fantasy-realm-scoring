package com.klamerek.fantasyrealms

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
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

fun isGooglePlayServicesUpToDate(activity: Context): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
    val apkVersion = googleApiAvailability.getApkVersion(activity)
    Log.d("Google API", "$apkVersion")
    return status == ConnectionResult.SUCCESS && apkVersion > 203000000
}

fun UiDevice.name(criteria: String): UiObject = this.name(criteria, 5)


fun UiDevice.name(criteria: String, numberOfTries: Int): UiObject {
    val timeout = 1000L
    var result: UiObject? = null
    var numberOfTriesRemaining = numberOfTries
    while ((result == null || !result.exists()) && numberOfTriesRemaining > 0) {
        numberOfTriesRemaining--
        result = this.findObject(UiSelector().description(criteria))
            .takeIf { it.waitForExists(timeout) } ?: this.findObject(UiSelector().text(criteria))
            .takeIf { it.waitForExists(timeout) } ?: this.childTextWith(criteria)
            .takeIf { it.waitForExists(timeout) }
    }
    return result?: this.findObject(UiSelector().description(criteria))
}


fun UiDevice.childTextWith(criteria: String): UiObject {
    val selector = childTextWithSelector(criteria)
    var result = this.findObject(selector)
    if (!result.exists()) {
        val scrollAsObject = this.findObject(UiSelector().scrollable(true))
        if (scrollAsObject.exists()) {
            val scroll = UiScrollable(scrollAsObject.selector)
            if (scroll.exists()){
                //Not logic but can happen on CDI :-(
                try {
                    scroll.scrollIntoView(selector)
                } catch(e: UiObjectNotFoundException){
                    Log.e("Util", "Could not find scroll")
                }
            }
            result = this.findObject(selector)
        }
    }
    return result
}

fun childTextWithSelector(criteria: String): UiSelector =
    UiSelector().childSelector(UiSelector().text(criteria))

fun UiObject.clickAndWaitForNewWindowIfExists(): Boolean =
    this.exists() && this.clickAndWaitForNewWindow()


/**
 * Method to ensure that Google App Services is up to date. <br>
 * Try to install it automatically using UIAutomator<br><br>
 * (After installation, cache must be cleared as well)<br><br>
 *
 * Minimum requirement is that your image must have Play store enabled.<br><br><br><br>
 *
 * Using UIAutomator is not a perfect option (this code is highly fragile depending of Android screen)
 * but I haven't found a better solution yet...
 *
 * @property activity context
 *
 */
fun ensureThatGooglePlayServicesUpToDate(activity: Context) {
    if (!isGooglePlayServicesUpToDate(activity)) {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()
        updateGooglePlayServices(device)
        device.pressHome()
        clearGooglePlayServicesAndApplicationCache(device)
        device.pressHome()
    }

    if (!isGooglePlayServicesUpToDate(activity)) {
        throw UnsupportedOperationException(
            "Device requires newer version of Google service (to use ML kit), " +
                    "please check readme if you have trouble to update it"
        )
    }
}

private fun updateGooglePlayServices(device: UiDevice) {
    val playStoreText = "Play Store"
    if (!device.name(playStoreText).exists()) {
        throw UnsupportedOperationException(
            "Emulator need 'Play store', please use an image that have it."
        )
    }
    device.name(playStoreText).clickAndWaitForNewWindow()
    if (device.name("Options").exists()) {
        device.name("Options").clickAndWaitForNewWindow()
        device.name("Updates").clickAndWaitForNewWindow()
    }
    device.name("Google Play services")
    val googlePlayServiceUpdateButton = device.findObjects(By.text("UPDATE"))
        .firstOrNull {
            it.parent.children
                .map { appComponent -> appComponent.text }
                .contains("Google Play services")
        }
    if (googlePlayServiceUpdateButton != null) {
        googlePlayServiceUpdateButton.click()
        UiScrollable(UiSelector().scrollable(true)).scrollToBeginning(100)
        while (device.name("STOP" ,1).exists()) {
            Log.i("Automatic updater","Download in progress...")
        }
        while (device.name("Installing...", 1).exists()) {
            Log.i("Automatic updater","Installation in progress...")
        }

    } else {
        Log.i("Automatic updater", "Application 'Google Play service' seems up to date")
    }
}

private fun clearGooglePlayServicesAndApplicationCache(device: UiDevice) {
    device.openQuickSettings()
    device.name("Open settings.").clickAndWaitForNewWindowIfExists()
    device.name("Storage").clickAndWaitForNewWindowIfExists()
    device.name("Internal shared storage", 2).clickAndWaitForNewWindowIfExists()
    device.name("Other apps").clickAndWaitForNewWindowIfExists()
    device.name("Google Play services").clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("Clear storage")).clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("CLEAR ALL DATA")).clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("OK")).clickAndWaitForNewWindowIfExists()
    device.pressBack()
    device.name("Fantasy Realms Scoring").clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("Clear storage")).clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("Clear storage")).clickAndWaitForNewWindowIfExists()
    device.findObject(UiSelector().clickable(true).textContains("OK")).clickAndWaitForNewWindowIfExists()

    device.pressHome()
}
