package com.klamerek.fantasyrealms.screen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.klamerek.fantasyrealms.LocaleManager
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings
import com.klamerek.fantasyrealms.databinding.ActivitySettingsBinding


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

        ArrayAdapter.createFromResource(
            this, R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter.notifyDataSetChanged()
            binding.languageSpinner.adapter = adapter
        }

        binding.doneButton.setOnClickListener {
            when (Strings.getArray(R.array.languagesCode)[binding.languageSpinner.selectedItemPosition]) {
                Strings.get(R.string.english_code) -> LocaleManager.saveLanguageInPreferences(baseContext, "en", "EN")
                Strings.get(R.string.french_code) -> LocaleManager.saveLanguageInPreferences(baseContext, "fr", "FR")
            }
            finish()
        }

    }

}
