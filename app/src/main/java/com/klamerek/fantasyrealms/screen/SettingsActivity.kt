package com.klamerek.fantasyrealms.screen

import android.os.Bundle
import android.widget.ArrayAdapter
import com.klamerek.fantasyrealms.databinding.ActivitySettingsBinding
import com.klamerek.fantasyrealms.util.Language
import com.klamerek.fantasyrealms.util.LocaleManager
import com.klamerek.fantasyrealms.util.LocaleManager.getLanguage
import com.klamerek.fantasyrealms.util.LocaleManager.languages
import com.klamerek.fantasyrealms.util.Preferences


/**
 * Settings activity
 *
 */
class SettingsActivity : CustomActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.displayCardNumberCheckBox.isChecked = Preferences.getDisplayCardNumber(baseContext)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        binding.doneButton.setOnClickListener {
            val language = binding.languageSpinner.adapter.getItem(binding.languageSpinner.selectedItemPosition) as Language
            LocaleManager.saveLanguageInPreferences(baseContext, language)
            Preferences.saveDisplayCardNumberInPreferences(baseContext, binding.displayCardNumberCheckBox.isChecked)
            finish()
        }

        binding.languageSpinner.setSelection((binding.languageSpinner.adapter as ArrayAdapter<Language>).getPosition(getLanguage(this)))

    }

}
