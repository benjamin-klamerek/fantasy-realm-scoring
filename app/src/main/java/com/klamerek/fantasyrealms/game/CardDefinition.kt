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
enum class Effect(private val displayId : Int) : Tag {
    BONUS(R.string.effect_bonus),
    CLEAR(R.string.effect_clear),
    PENALTY(R.string.effect_penalty),
    BLANK(R.string.effect_blank);

    override fun label(): String = this.name
    override fun display(): String = Strings.get(displayId)
}

/**
 * Suit of card (family)
 *
 * @property displayId      value to display on screens
 */
enum class Suit(private val displayId : Int, val color: Int) : Tag {
    ARMY(R.string.suit_army, R.color.colorArmy), ARTIFACT(R.string.suit_artifact, R.color.colorArtefact), BEAST(R.string.suit_beast, R.color.colorBeast),
    FLAME(R.string.suit_flame, R.color.colorFlame), FLOOD(R.string.suit_flood, R.color.colorFlood), LAND(R.string.suit_land, R.color.colorLand),
    LEADER(R.string.suit_leader, R.color.colorLeader), WEAPON(R.string.suit_weapon, R.color.colorWeapon), WEATHER(R.string.suit_weather, R.color.colorWeather),
    WILD(R.string.suit_wild, R.color.colorWild), WIZARD(R.string.suit_wizard, R.color.colorWizard);

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
class CardDefinition(val id: Int, private val keyName: Int, val value: Int, val suit: Suit, private val keyRule: Int) {

    fun isOneOf(vararg suit: Suit) = suit.contains(this.suit)

    fun name() = Strings.get(keyName)

    fun rule() = Strings.get(keyRule)

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