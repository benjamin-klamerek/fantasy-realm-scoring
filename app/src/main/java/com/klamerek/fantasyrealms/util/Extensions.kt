package com.klamerek.fantasyrealms

import android.content.res.ColorStateList
import com.klamerek.fantasyrealms.game.*
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
fun ColorStateList.revertChipColorState(): ColorStateList {
    return ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
        intArrayOf(
            this.getColorForState(intArrayOf(), -1),
            this.getColorForState(intArrayOf(android.R.attr.state_checked), -1)
        )
    )
}

// All collections methods - START

fun Iterable<Card>.rules(): List<Rule<*>> {
    return flatMap {
        return@flatMap (it.rules())
    }
}

fun Iterable<Rule<*>>.asRuleAboutRule(): List<RuleAboutRule?> {
    return map {
        return@map (it as? RuleAboutRule)
    }
}

fun Iterable<Rule<*>>.asRuleAboutCard(): List<RuleAboutCard?> {
    return map {
        return@map (it as? RuleAboutCard)
    }
}

fun Sequence<Rule<*>>.asRuleAboutScore(): Sequence<RuleAboutScore?> {
    return map {
        return@map (it as? RuleAboutScore)
    }
}

fun Iterable<Rule<*>>.activated(card: Card): List<Rule<*>> {
    return filter {
        return@filter (card.isActivated(it))
    }
}

fun Sequence<Rule<*>>.activated(card: Card): Sequence<Rule<*>> {
    return filter {
        return@filter (card.isActivated(it))
    }
}

fun <T : Rule<*>?> Iterable<T>.with(tag: Tag): List<T> {
    return filter {
        return@filter (it?.tags?.contains(tag) == true)
    }
}

fun <T : Rule<*>?> Sequence<T>.with(tag: Tag): Sequence<T> {
    return filter {
        return@filter (it?.tags?.contains(tag) == true)
    }
}

fun <T : RuleAboutScore?> Sequence<T>.score(game: Game): Int {
    return map { rule -> rule?.logic?.invoke(game) }
        .sumOf { any -> if (any is Int) any else 0 }
}

fun <T : RuleAboutCard?> Iterable<T>.listCards(game: Game): List<Card> {
    return flatMap { rule -> rule?.logic?.invoke(game).orEmpty() }
}

fun <T : RuleAboutRule?> Iterable<T>.listRules(game: Game): List<Rule<*>> {
    return flatMap { rule -> rule?.logic?.invoke(game).orEmpty() }
}


// All collections methods - END
