package com.klamerek.fantasyrealms

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


