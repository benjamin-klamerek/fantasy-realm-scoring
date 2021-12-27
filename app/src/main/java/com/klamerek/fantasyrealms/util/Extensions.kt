package com.klamerek.fantasyrealms

import android.content.res.ColorStateList
import java.text.Normalizer

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

/**
 * To remove all accents (replaced by the "basic" letter) in a string
 *
 * @return  new string with replacement done
 */
fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

/**
 * Convert Boolean to Int
 *
 * @return 1 for true, O for false
 */
fun Boolean.toInt() = if (this) 1 else 0

/**
 * Invert ColorStateList for chip with 2 states
 * @return inverted ColorStateList
 */
fun ColorStateList.revertChipColorState(): ColorStateList{
    return ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
        intArrayOf(this.getColorForState(intArrayOf(), -1),
            this.getColorForState(intArrayOf(android.R.attr.state_checked), -1)))
}


