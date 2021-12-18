package com.klamerek.fantasyrealms.screen

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.klamerek.fantasyrealms.game.CardDefinitions
import com.klamerek.fantasyrealms.game.Suit

/**
 * Function that can "upgrade" a rule display by bolding and coloring suits and card names
 */
fun colorSuitsAndBoldCardNames(context: Context, text: String): SpannableString {
    val sourceCleaned = text.replace("'", " ").replace(",", " ")
    val spannable = SpannableString(text)

    Suit.values().forEach { suit ->
        generatePatternFromName(suit.display())
            .mapNotNull(searchRanges(sourceCleaned))
            .onEach(applyColor(spannable, context, suit.color))
            .onEach(applyBold(spannable))
    }

    CardDefinitions.getAll().forEach { definition ->
        generatePatternFromName(definition.name())
            .mapNotNull(searchRanges(sourceCleaned))
            .onEach(applyBold(spannable))
    }

    return spannable
}

private fun generatePatternFromName(name: String) =
    listOf("\\b$name\\b", "\\b" + name + "s\\b")

private fun applyColor(
    spannable: SpannableString,
    context: Context,
    color: Int
) = { range: IntRange ->
    setSpan(
        spannable,
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        range
    )
}

private fun applyBold(spannable: SpannableString) =
    { range: IntRange -> setSpan(spannable, StyleSpan(Typeface.BOLD), range) }

private fun searchRanges(sourceCleaned: String) =
    { pattern: String -> pattern.toRegex().find(sourceCleaned, 0)?.range }

private fun setSpan(spannable: SpannableString, what: Any, range: IntRange) {
    spannable.setSpan(what, range.first, range.last + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
}
