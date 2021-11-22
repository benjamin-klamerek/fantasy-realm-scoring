package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings

/**
 * A tag is only a label attached to a rule
 *
 */
interface Tag {
    fun label(): String
    fun display(): String
}

/**
 * Rule effect (like bonus, penalty, ...)
 *
 * @property displayId      value to display on screens
 */
enum class Effect(private val displayId: Int) : Tag {
    BONUS(R.string.effect_bonus),
    CLEAR(R.string.effect_clear),
    PENALTY(R.string.effect_penalty),
    BLANK(R.string.effect_blank),
    UNBLANKABLE(R.string.effect_unbankable);

    override fun label(): String = this.name
    override fun display(): String = Strings.get(displayId)
}

@Suppress("MagicNumber")
enum class CardSet(val numberOfCards: Int) {
    BASE(53),
    PROMO(1),
    CURSED_HOARD(47)
}

/**
 * Suit of card (family)
 *
 * @property displayId      value to display on screens
 */
enum class Suit(private val displayId: Int, val color: Int, val set: CardSet) : Tag {
    ARMY(R.string.suit_army, R.color.colorArmy, CardSet.BASE),
    ARTIFACT(R.string.suit_artifact, R.color.colorArtifact, CardSet.BASE),
    BEAST(R.string.suit_beast, R.color.colorBeast, CardSet.BASE),
    FLAME(R.string.suit_flame, R.color.colorFlame, CardSet.BASE),
    FLOOD(R.string.suit_flood, R.color.colorFlood, CardSet.BASE),
    LAND(R.string.suit_land, R.color.colorLand, CardSet.BASE),
    LEADER(R.string.suit_leader, R.color.colorLeader, CardSet.BASE),
    WEAPON(R.string.suit_weapon, R.color.colorWeapon, CardSet.BASE),
    WEATHER(R.string.suit_weather, R.color.colorWeather, CardSet.BASE),
    WILD(R.string.suit_wild, R.color.colorWild, CardSet.BASE),
    WIZARD(R.string.suit_wizard, R.color.colorWizard, CardSet.BASE),
    BUILDING(R.string.suit_building, R.color.colorBuilding, CardSet.CURSED_HOARD),
    OUTSIDER(R.string.suit_outsider, R.color.colorOutsider, CardSet.CURSED_HOARD),
    UNDEAD(R.string.suit_undead, R.color.colorUndead, CardSet.CURSED_HOARD),
    CURSED_ITEM(R.string.suit_cursed_item, R.color.colorCursedItem, CardSet.CURSED_HOARD);

    override fun label(): String = this.name
    override fun display(): String = Strings.get(displayId)
}

/**
 * Immutable definition of a card
 *
 * @property id         card id
 * @property name       key card name
 * @property value      base value for scoring
 * @property suit       card family
 * @property keyRule    key explanation of the rule as described on the physical card
 */
open class CardDefinition(
    val id: Int, private val keyName: Int,
    val value: Int, val suit: Suit, private val keyRule: Int,
    private val cardSet: CardSet
) {

    fun isOneOf(vararg suit: Suit) = suit.contains(this.suit)

    fun name() = Strings.get(keyName)

    fun rule() = Strings.get(keyRule)

    fun nameWithId() = when (cardSet) {
        CardSet.PROMO -> name() + " (Promo)"
        CardSet.CURSED_HOARD -> name() + " (" + (id - CardSet.BASE.numberOfCards) + "/" + cardSet.numberOfCards + ")"
        else -> name() + " (" + id + "/" + cardSet.numberOfCards + ")"
    }

    override fun toString(): String {
        return name() + " " + rule()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardDefinition

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}



