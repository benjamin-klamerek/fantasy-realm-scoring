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
    return status == ConnectionResult.SUCCESS && apkVersion > 203000000
}

fun UiDevice.descriptionStartsWith(criteria: String): UiObject =
    this.findObject(UiSelector().descriptionStartsWith(criteria))

fun UiDevice.textStartsWith(criteria: String): UiObject =
    this.findObject(UiSelector().textStartsWith(criteria))

fun UiDevice.clickableChildTextContains(id: String): UiObject =
    this.findObject(UiSelector().clickable(true).childSelector(UiSelector().textContains(id)))

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
        clearGooglePlayServicesCache(device)
        device.pressHome()
        Thread.sleep(5000)
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
    if (!device.descriptionStartsWith(playStoreText).exists()) {
        throw UnsupportedOperationException(
            "Emulator need 'Play store', please use an image that have it."
        )
    }
    Thread.sleep(5000)
    val playStoreButton = device.descriptionStartsWith(playStoreText)
    playStoreButton.clickAndWaitForNewWindow()
    Thread.sleep(5000)
    if (device.descriptionStartsWith("Options").exists()) {
        device.descriptionStartsWith("Options").clickAndWaitForNewWindow()
        Thread.sleep(5000)
        device.textStartsWith("Updates").clickAndWaitForNewWindow()
    }
    while (device.textStartsWith("Checking").exists()) {
        Thread.sleep(3000)
    }
    Thread.sleep(3000)
    val scrollBar = UiScrollable(UiSelector().scrollable(true))
    scrollBar.scrollIntoView(UiSelector().textContains("Google Play services"))

    val googlePlayServiceUpdateButton = device.findObjects(By.text("UPDATE"))
        .firstOrNull {
            it.parent.children
                .map { appComponent -> appComponent.text }
                .contains("Google Play services")
        }
    if (googlePlayServiceUpdateButton != null) {
        googlePlayServiceUpdateButton.click()
        scrollBar.scrollToBeginning(100)
        while (device.textStartsWith("stop").exists() || device.textStartsWith("Installing").exists()) {
            Log.d("Automatic updater", "Updating in progress (may take a long time)...")
            Thread.sleep(5000)
        }
    } else {
        Log.d("Automatic updater", "Application 'Google Play service' seems up to date")
    }
}

private fun clearGooglePlayServicesCache(device: UiDevice) {
    device.openQuickSettings()
    device.descriptionStartsWith("Open settings").clickAndWaitForNewWindow()
    device.clickableChildTextContains("Storage").clickAndWaitForNewWindow()
    device.clickableChildTextContains("Other apps").clickAndWaitForNewWindow()
    device.clickableChildTextContains("Google Play services").clickAndWaitForNewWindow()
    device.findObject(UiSelector().clickable(true).textContains("Clear cache")).clickAndWaitForNewWindow()
}

