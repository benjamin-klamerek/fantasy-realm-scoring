package com.klamerek.fantasyrealms.screen

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.klamerek.fantasyrealms.game.Suit
import com.klamerek.fantasyrealms.game.allDefinitions

/**
 * Function that can "upgrade" a rule display by bolding and coloring suits and card names
 */
fun colorSuitsAndBoldCardNames(context: Context, text: String): SpannableString {
    val sourceCleaned = text.replace("'", " ").replace(",", " ")
    val spannable = SpannableString(text)

    Suit.values().forEach { suit ->
        ("\\b" + suit.display() + "\\b").toRegex().find(sourceCleaned, 0)?.range?.let { range ->
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, suit.color)),
                range.first, range.last + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                range.first, range.last + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    allDefinitions.forEach { definition ->
        ("\\b" + definition.name() + "\\b").toRegex().find(sourceCleaned, 0)?.range?.let { range ->
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                range.first, range.last + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    return spannable
}
