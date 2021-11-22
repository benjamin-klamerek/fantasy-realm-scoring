package com.klamerek.fantasyrealms.screen

import android.os.Bundle
import android.widget.ArrayAdapter
import com.klamerek.fantasyrealms.databinding.ActivitySettingsBinding
import com.klamerek.fantasyrealms.game.CardDefinitions
import com.klamerek.fantasyrealms.game.Player
import com.klamerek.fantasyrealms.toInt
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
        binding.withBuildingsOutsidersUndeadCheckBox.isChecked =
            Preferences.getBuildingsOutsidersUndead(baseContext)
        binding.withCursedItemsCheckBox.isChecked = Preferences.getCursedItems(baseContext)

        val initialValue = getCardScopeId()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        binding.doneButton.setOnClickListener {
            val language =
                binding.languageSpinner.adapter.getItem(binding.languageSpinner.selectedItemPosition) as Language
            LocaleManager.saveLanguageInPreferences(baseContext, language)
            Preferences.saveDisplayCardNumberInPreferences(
                baseContext,
                binding.displayCardNumberCheckBox.isChecked
            )
            Preferences.saveBuildingsOutsidersUndeadInPreferences(
                baseContext,
                binding.withBuildingsOutsidersUndeadCheckBox.isChecked
            )
            Preferences.saveCursedItemsInPreferences(
                baseContext,
                binding.withCursedItemsCheckBox.isChecked
            )
            removeCardOutOfScope(initialValue)
            finish()
        }

        binding.languageSpinner.setSelection(
            (binding.languageSpinner.adapter as ArrayAdapter<Language>).getPosition(
                getLanguage(this)
            )
        )

    }

    /**
     * If card scope changed, we remove obsolete cards
     */
    private fun removeCardOutOfScope(initialValue: String) {
        if (initialValue != getCardScopeId()) {
            val scope = CardDefinitions.get(baseContext)
            Player.all.forEach { player ->
                player.game.cards().map { it.definition }
                    .filter { !scope.contains(it) }.toList()
                    .forEach { cardToRemove -> player.game.remove(cardToRemove) }
            }
        }
    }

    private fun getCardScopeId() =
        binding.withBuildingsOutsidersUndeadCheckBox.isChecked.toInt().toString() +
                binding.withCursedItemsCheckBox.isChecked.toInt().toString()

}
