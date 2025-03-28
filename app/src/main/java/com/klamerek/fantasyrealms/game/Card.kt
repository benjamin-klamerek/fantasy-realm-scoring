package com.klamerek.fantasyrealms.game

/**
 * Mutable representation of a card in a player hand.<br>
 * handle the effective status of the card in the game (like blanked or partial activated)
 *
 * @property definition card definition
 * @property rules      rules assigned to this card
 */
class Card(val definition: CardDefinition, private val rules: List<Rule<*>>) {

    private var simulatedName: String? = null
    private var simulatedValue: Int? = null
    private var simulatedSuit: Suit? = null
    private var simulatedRules: List<Rule<*>>? = null
    var blanked: Boolean = false
    private val ruleDeactivated: ArrayList<Rule<*>> = ArrayList()
    private val temporaryRules: ArrayList<Rule<*>> = ArrayList()

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

    fun addTemporaryRule(rule: Rule<*>) = temporaryRules.add(rule)

    fun deactivate(rule: Rule<*>) = ruleDeactivated.add(rule)

    fun isActivated(rule: Rule<*>): Boolean = !ruleDeactivated.contains(rule)

    fun isOneOf(vararg suit: Suit) = suit.contains(this.suit()) ||
            definition.additionalSuits.any { suit.contains(it) }

    fun hasSameNameThan(definition: CardDefinition) = this.name() == definition.name()

    fun name(): String = simulatedName ?: definition.name()

    fun value(): Int = simulatedValue ?: definition.value

    fun suit(): Suit = simulatedSuit ?: definition.suit

    fun isOdd(): Boolean = value() % 2 == 1

    fun rules(): List<Rule<*>> = listOf(simulatedRules ?: rules, temporaryRules).flatten()

    fun name(name: String) {
        this.simulatedName = name
    }

    fun value(value: Int) {
        this.simulatedValue = value
    }

    fun suit(suit: Suit?) {
        this.simulatedSuit = suit
    }

    fun rules(rules: List<Rule<*>>) {
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

    fun transitionName(): String {
        return "transition_chip_" + definition.id
    }


}
