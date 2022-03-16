package com.klamerek.fantasyrealms.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.klamerek.fantasyrealms.databinding.ActivityCardsSelectionBinding
import com.klamerek.fantasyrealms.game.*
import com.klamerek.fantasyrealms.revertChipColorState
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Preferences
import java.io.Serializable

class CardsSelectionActivity : CustomActivity() {

    private lateinit var input: CardsSelectionExchange
    private lateinit var binding: ActivityCardsSelectionBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardsSelectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        input =
            intent.getSerializableExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID) as CardsSelectionExchange

        invertSelectionColorMode(baseContext)
        updateVisibleChips(baseContext)
        updateMainLabel()
        checkSelectedCards()
        checkSelectedSuits()

        adaptCardListDisplay()
        adaptSuitListDisplay()
        updateMainButtonStatus()

        if (Preferences.getDisplayCardNumber(baseContext)) {
            binding.chipGroup.children.toList().forEach {
                (it as? Chip)?.text =
                    CardDefinitions.getAllById()[it.tag.toString().toIntOrNull() ?: 0]?.nameWithId()
                        ?: ""
            }
        }

        binding.addCardsButton.setOnClickListener {
            val closingIntent = Intent()
            val answer = CardsSelectionExchange()
            answer.cardInitiator = input.cardInitiator
            answer.cardsSelected = cardChips().filter { chip -> chip.isChecked }
                .map { chip -> chip.tag }.mapNotNull { tag -> Integer.valueOf(tag.toString()) }
                .toMutableList()
            answer.suitsSelected = suitChips().filter { chip -> chip.isChecked }
                .mapNotNull { chip -> chip.tag.toString() }.toMutableList()
            closingIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, answer)
            setResult(Constants.RESULT_OK, closingIntent)
            finish()
        }

    }

    private fun invertSelectionColorMode(context: Context) {
        if (Preferences.getDisplayChipColorOnSearch(context)){
            cardChips().plus(suitChips()).forEach { chip ->
                run {
                    chip.chipBackgroundColor = chip.chipBackgroundColor?.revertChipColorState()
                    chip.setTextColor(chip.textColors.revertChipColorState())
                    chip.chipStrokeColor = chip.chipStrokeColor?.revertChipColorState()
                }
            }
        }
    }

    private fun updateVisibleChips(context: Context) {
        val activeDefinitions = CardDefinitions.get(context).map { it.id.toString() }
        cardChips().forEach { chip ->
            chip.visibility =
                if (activeDefinitions.contains(chip.tag.toString())) View.VISIBLE else View.GONE
        }

        suitChips().forEach { chip ->
            chip.visibility = when (Suit.valueOf(chip.tag.toString()).set) {
                CardSet.CURSED_HOARD -> if (Preferences.getBuildingsOutsidersUndead(context)) View.VISIBLE else View.GONE
                else -> View.VISIBLE
            }
        }
    }

    private fun checkSelectedSuits() {
        suitChips().forEach { chip ->
            chip.isChecked = input.suitsSelected.contains(chip.tag.toString())
        }
    }

    private fun adaptSuitListDisplay() {
        val suitActivated =
            input.selectionMode == Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
        binding.suitScrollView.visibility = if (suitActivated) View.VISIBLE else View.GONE
        binding.divider.visibility = if (suitActivated) View.VISIBLE else View.GONE
        suitChips().forEach { chip -> chip.setOnClickListener(onlyOneSuitSelected()) }
    }

    private fun adaptCardListDisplay() {
        if (input.selectionMode != Constants.CARD_LIST_SELECTION_MODE_DEFAULT) {
            showOnlyPotentialCandidates()
            cardChips().forEach { chip -> chip.setOnClickListener(onlyOneCardSelected()) }
        } else {
            if (Preferences.getRemoveAlreadySelected(baseContext)){
                displayOnlyRemainingCards()
            }
        }
    }

    private fun displayOnlyRemainingCards() {
        val alreadySelected = DiscardArea.instance.game().cards().map { it.definition.id }
            .plus(Player.all.flatMap { it.game().cards().map { card -> card.definition.id } })
            .minus(input.cardsSelected.toSet())
        cardChips().forEach { chip ->
            chip.visibility = if (alreadySelected.contains(Integer.valueOf(chip.tag.toString())))
                View.GONE else View.VISIBLE
        }
    }

    private fun updateMainLabel() {
        binding.selectionLabel.text =
            colorSuitsAndBoldCardNames(applicationContext, input.label.orEmpty())
    }

    private fun onlyOneSuitSelected(): View.OnClickListener {
        return View.OnClickListener {
            suitChips().filter { chip -> chip != it }.forEach { chip -> chip.isChecked = false }
            updateMainButtonStatus()
        }
    }

    private fun onlyOneCardSelected(): View.OnClickListener {
        return View.OnClickListener {
            cardChips().filter { chip -> chip != it }.forEach { chip -> chip.isChecked = false }
            updateMainButtonStatus()
        }
    }

    private fun updateMainButtonStatus() {
        when (input.selectionMode) {
            Constants.CARD_LIST_SELECTION_MODE_DEFAULT -> binding.addCardsButton.isEnabled = true
            Constants.CARD_LIST_SELECTION_MODE_ONE_CARD ->
                binding.addCardsButton.isEnabled = cardChips().count { chip -> chip.isChecked } == 1
            Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT ->
                binding.addCardsButton.isEnabled =
                    cardChips().count { chip -> chip.isChecked } == 1 &&
                            suitChips().count { chip -> chip.isChecked } == 1

        }
    }

    private fun showOnlyPotentialCandidates() {
        cardChips().forEach { chip ->
            chip.visibility =
                if (input.cardsScope.contains(Integer.valueOf(chip.tag.toString()))) View.VISIBLE else View.GONE
        }
    }

    private fun checkSelectedCards() {
        cardChips().forEach { chip ->
            chip.isChecked = input.cardsSelected.contains(Integer.valueOf(chip.tag.toString()))
        }
    }

    private fun cardChips() =
        binding.chipGroup.children.filter { child -> child is Chip }.map { child -> child as Chip }

    private fun suitChips() = binding.suitChipGroup.children.filter { child -> child is Chip }
        .map { child -> child as Chip }
}

/**
 * POJO to transfer in and out data from cards selection activity
 *
 * @property selectionMode              can be many cards or one card or or card and a suit
 * @property label                      if set, display a label on the top
 * @property cardInitiator              card initiator of the selection (special rules), transferred when activity is finished
 * @property cardsSelected              indicates which cards must already displayed as selected
 * @property suitsSelected               indicates which suits must already displayed as selected
 * @property cardsScope                 indicates which cards must be accessible for selection
 */
class CardsSelectionExchange : Serializable {
    var selectionMode: Int = Constants.CARD_LIST_SELECTION_MODE_DEFAULT
    var label: String? = null
    var cardInitiator: Int? = null
    var cardsSelected: MutableList<Int> = ArrayList()
    var suitsSelected: MutableList<String> = ArrayList()
    val cardsScope: MutableList<Int> = ArrayList()
}
