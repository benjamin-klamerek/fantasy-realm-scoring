package com.klamerek.fantasyrealms.game

/**
 * Mutable representation of a card in a player hand.<br>
 * handle the effective status of the card in the game (like blanked or partial activated)
 *
 * @property definition card definition
 * @property rules      rules assigned to this card
 */
class Card(val definition: CardDefinition, private val rules: List<Rule<out Any>>) {

    private var simulatedName: String? = null
    private var simulatedValue: Int? = null
    private var simulatedSuit: Suit? = null
    private var simulatedRules: List<Rule<out Any>>? = null
    var blanked: Boolean = false
    private val ruleDeactivated: ArrayList<Rule<out Any>> = ArrayList()
    private val temporaryRules: ArrayList<Rule<out Any>> = ArrayList()

    fun clear() {
        temporaryRules.clear()
        ruleDeactivated.clear()
        clearSimulation()
        blanked = false
    }

    private fun clearSimulation() {
        simulatedName = null
        simulatedValue = null
        simulatedSuit = null
        simulatedRules = null
    }

    fun addTemporaryRule(rule: Rule<out Any>) = temporaryRules.add(rule)

    fun deactivate(rule: Rule<out Any>) = ruleDeactivated.add(rule)

    fun isActivated(rule: Rule<out Any>): Boolean = !ruleDeactivated.contains(rule)

    fun isOneOf(vararg suit: Suit) = suit.contains(this.suit()) ||
            definition.additionalSuits.any { suit.contains(it) }

    fun hasSameNameThan(definition: CardDefinition) = this.name() == definition.name()

    fun name(): String = simulatedName ?: definition.name()

    fun value(): Int = simulatedValue ?: definition.value

    fun suit(): Suit = simulatedSuit ?: definition.suit

    fun isOdd(): Boolean = value() % 2 == 1

    fun rules(): List<Rule<out Any>> = listOf(simulatedRules ?: rules, temporaryRules).flatten()

    fun name(name: String) {
        this.simulatedName = name
    }

    fun value(value: Int) {
        this.simulatedValue = value
    }

    fun suit(suit: Suit?) {
        this.simulatedSuit = suit
    }

    fun rules(rules: List<Rule<out Any>>) {
        this.simulatedRules = rules
    }

    override fun toString(): String = name()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (name() != other.name()) return false

        return true
    }

    override fun hashCode(): Int {
        return name().hashCode()
    }


}
