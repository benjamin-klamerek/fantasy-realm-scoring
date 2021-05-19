package com.klamerek.fantasyrealms.screen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.klamerek.fantasyrealms.LocaleManager

abstract class CustomActivity : AppCompatActivity() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.updateContextWithPreferredLanguage(base!!))
    }

}

