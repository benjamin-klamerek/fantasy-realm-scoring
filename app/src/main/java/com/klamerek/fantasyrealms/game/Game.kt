package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.util.Constants
import java.lang.Integer.max

/**
 * List of cards (player hand) wth scoring calculation
 *
 */
class Game {

    private val cards = ArrayList<Card>()
    private val bonusScoreByCard = HashMap<CardDefinition, Int>()
    private val penaltyScoreByCard = HashMap<CardDefinition, Int>()

    var bookOfChangeSelection: Pair<CardDefinition?, Suit?>? = null
    var islandSelection: CardDefinition? = null
    var shapeShifterSelection: CardDefinition? = null
    var mirageSelection: CardDefinition? = null
    var doppelgangerSelection: CardDefinition? = null

    fun cards(): Collection<Card> {
        return ArrayList(cards)
    }

    fun cardsNotBlanked(): Collection<Card> {
        return cards.filter { card -> !card.blanked }
    }

    fun add(cardDefinition: CardDefinition) {
        if (!cards.map { card -> card.definition }.contains(cardDefinition)) {
            cards.add(Card(cardDefinition, AllRules.instance[cardDefinition].orEmpty()))
        }
    }

    fun remove(cardDefinition: CardDefinition) {
        cards.removeIf{ it.definition == cardDefinition }
        if (cardDefinition == bookOfChanges || cardDefinition == bookOfChangeSelection?.first) {
            bookOfChangeSelection = null
        }
        if (cardDefinition == island || cardDefinition == islandSelection) {
            islandSelection = null
        }
        if (cardDefinition == shapeshifter || cardDefinition == shapeShifterSelection) {
            shapeShifterSelection = null
        }
        if (cardDefinition == mirage || cardDefinition == mirageSelection) {
            mirageSelection = null
        }
        if (cardDefinition == doppelganger || cardDefinition == doppelgangerSelection) {
            doppelgangerSelection = null
        }
    }

    fun update(cardDefinitions: List<CardDefinition>) {
        cardDefinitions.forEach { definition -> add(definition) }
        cards.map { card -> card.definition }.filter { definition -> !cardDefinitions.contains(definition) }
            .forEach { definition -> remove(definition) }
    }

    fun clear() {
        cards.clear()
    }

    fun countOddCard() = cardsNotBlanked().count { it.isOdd() }

    fun isAllOdd() = cardsNotBlanked().all { it.isOdd() }

    fun countCard(suit: Suit) = cardsNotBlanked().count { it.isOneOf(suit) }

    fun noCard(suit: Suit) = countCard(suit) == 0

    fun atLeastOne(suit: Suit) = countCard(suit) > 0


    /**
     * Remark : this method uses comparison by name to handle when a wild card simulate another card.
     *
     * @param cardExpected      list of cards expected in the game
     */
    fun contains(vararg cardExpected: CardDefinition) = cardsNotBlanked()
        .map { it.name() }.containsAll(cardExpected.toList().map { it.name() })

    fun longestSuite(): Int {
        val sorted = cardsNotBlanked().sortedBy { card -> card.value() }
        var maxCount = 1
        var count = 1
        var previousValue = Int.MIN_VALUE
        for (card in sorted) {
            if (card.value() == (previousValue + 1)) {
                count += 1
            } else if (card.value() != previousValue) {
                count = 1
            }
            maxCount = max(count, maxCount)
            previousValue = card.value()
        }
        return maxCount
    }

    fun largestSuit(): Int {
        return cardsNotBlanked().groupBy { card -> card.suit() }.map { entry -> entry.value.size }.maxOrNull() ?: 0
    }

    fun calculate() {
        cards.forEach { card -> card.clear() }

        applySpecificCardEffects()

        val penaltiesToDeactivate = cards.map { card -> identifyClearedRules(card) }.flatten()
        penaltiesToDeactivate.forEach { ruleToDeactivate ->
            cards.forEach { card ->
                card.rules()
                    .filter { rule -> rule == ruleToDeactivate }
                    .forEach { rule -> card.deactivate(rule) }
            }
        }

        val blankedCardFirstLevel = cards.map { card -> identifyBlankedCards(card) }.flatten()
        blankedCardFirstLevel.forEach { card -> card.blanked = true }
        cardsNotBlanked().map { card -> identifyBlankedCards(card) }.flatten().forEach { card -> card.blanked = true }

        bonusScoreByCard.clear()
        penaltyScoreByCard.clear()
        bonusScoreByCard.putAll(cardsNotBlanked().map { card ->
            card.definition to card.rules()
                .asSequence()
                .filter { rule -> card.isActivated(rule) }
                .map { rule -> rule as? RuleAboutScore }
                .filter { rule -> rule?.tags?.contains(Effect.BONUS) ?: false }
                .map { rule -> rule?.logic?.invoke(this) }
                .sumOf { any -> if (any is Int) any else 0 }
        }.toMap())
        penaltyScoreByCard.putAll(cardsNotBlanked().map { card ->
            card.definition to card.rules()
                .asSequence()
                .filter { rule -> card.isActivated(rule) }
                .map { rule -> rule as? RuleAboutScore }
                .filter { rule -> rule?.tags?.contains(Effect.PENALTY) ?: false }
                .map { rule -> rule?.logic?.invoke(this) }
                .sumOf { any -> if (any is Int) any else 0 }
        }.toMap())
    }

    private fun applySpecificCardEffects() {
        doppelgangerSelection?.let { cardDefinition -> applyDoppelganger(cardDefinition) }
        mirageSelection?.let { cardDefinition -> applyMirage(cardDefinition) }
        shapeShifterSelection?.let { cardDefinition -> applyShapeShifter(cardDefinition) }
        bookOfChangeSelection?.let { pair -> applyBookOfChanges(pair.first, pair.second) }
        islandSelection?.let { cardDefinition -> applyIsland(cardDefinition) }
    }

    fun score(): Int = bonusScoreByCard.entries.sumOf { it.value } +
            penaltyScoreByCard.entries.sumOf { it.value } +
            cardsNotBlanked().sumOf { it.value() }

    fun score(card: CardDefinition): Int = bonusScore(card) +
            penaltyScore(card) +
            cardsNotBlanked().filter { it.definition == card }.sumOf { it.value() }

    fun bonusScore(card: CardDefinition): Int = bonusScoreByCard.getOrDefault(card, 0)

    fun penaltyScore(card: CardDefinition): Int = penaltyScoreByCard.getOrDefault(card, 0)

    private fun identifyBlankedCards(card: Card): List<Card> =
        card.rules().filter { rule -> card.isActivated(rule) }
            .filter { rule -> rule.tags.contains(Effect.BLANK) }
            .map { rule -> rule as? RuleAboutCard }
            .flatMap { rule -> rule?.logic?.invoke(this).orEmpty() }

    private fun identifyClearedRules(card: Card): List<Rule<out Any>> =
        card.rules().filter { rule -> card.isActivated(rule) }
            .filter { rule -> rule.tags.contains(Effect.CLEAR) }
            .map { rule -> rule as? RuleAboutRule }
            .flatMap { rule -> rule?.logic?.invoke(this).orEmpty() }


    fun identifyBlankedCards(scope: (Card) -> Boolean): List<Card> {
        return this.cardsNotBlanked().filter(scope)
    }

    fun identifyClearedPenalty(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return identifyRule(scope, Effect.PENALTY)
    }

    fun identifyArmyClearedPenalty(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return identifyRule(scope, Effect.PENALTY, Suit.ARMY)
    }

    private fun identifyRule(scope: (Card) -> Boolean, vararg tags: Tag): List<Rule<out Any>> {
        return cardsNotBlanked().filter(scope)
            .flatMap { card -> card.rules() }
            .filter { rule -> rule.tags.containsAll(tags.asList()) }
    }

    fun bookOfChangesCandidates(): List<CardDefinition> {
        return cards.map { card -> card.definition }.filter { definition -> definition != bookOfChanges }
    }

    private fun applyBookOfChanges(cardToUpdate: CardDefinition?, newSuit: Suit?) =
        cards.filter { card -> card.definition == cardToUpdate }
            .map { card -> card.suit(newSuit) }

    fun islandCandidates(): List<CardDefinition> {
        return cards.filter { card -> card.isOneOf(Suit.FLOOD, Suit.FLAME) }.map { card -> card.definition }
    }

    private fun applyIsland(cardToClean: CardDefinition) {
        if (islandCandidates().contains(cardToClean)) {
            cards.filter { card -> card.definition == cardToClean }
                .map { card ->
                    card.rules()
                        .filter { rule -> rule.tags.contains(Effect.PENALTY) }
                        .forEach { rule -> card.deactivate(rule) }
                }
        }
    }

    fun shapeShifterCandidates(): List<CardDefinition> {
        return allDefinitions.filter { definition -> definition.isOneOf(Suit.ARTIFACT, Suit.LEADER, Suit.WIZARD, Suit.WEAPON, Suit.BEAST) }
    }

    private fun applyShapeShifter(cardToCopy: CardDefinition) {
        if (shapeShifterCandidates().contains(cardToCopy)) {
            cards.filter { card -> card.definition == shapeshifter }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    fun mirageCandidates(): List<CardDefinition> {
        return allDefinitions.filter { definition -> definition.isOneOf(Suit.ARMY, Suit.LAND, Suit.WEATHER, Suit.FLOOD, Suit.FLAME) }
    }

    private fun applyMirage(cardToCopy: CardDefinition) {
        if (mirageCandidates().contains(cardToCopy)) {
            cards.filter { card -> card.definition == mirage }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    fun doppelgangerCandidates(): List<CardDefinition> =
        cards.map { card -> card.definition }.filter { definition -> definition != doppelganger }

    private fun applyDoppelganger(cardToCopy: CardDefinition) {
        if (doppelgangerCandidates().contains(cardToCopy)) {
            cards.filter { card -> card.definition == doppelganger }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                    card.value(cardToCopy.value)
                    card.rules(AllRules.instance[cardToCopy].orEmpty().filter { rule -> rule.tags.contains(Effect.PENALTY) })
                }
        }
    }

    fun handSizeExpected(): Int {
        return Constants.DEFAULT_HAND_SIZE + cards.map { card -> card.definition }.filter { definition -> definition == necromancer }.count()
    }

    fun actualHandSize(): Int {
        return cards.size
    }

    fun ruleEffectCardSelectionAbout(cardDefinition: CardDefinition?): List<CardDefinition> {
        return when (cardDefinition) {
            doppelganger -> listOfNotNull(doppelgangerSelection)
            mirage -> listOfNotNull(mirageSelection)
            shapeshifter -> listOfNotNull(shapeShifterSelection)
            bookOfChanges -> listOfNotNull(bookOfChangeSelection?.first)
            island -> listOfNotNull(islandSelection)
            else -> emptyList()
        }
    }

    fun ruleEffectSuitSelectionAbout(cardDefinition: CardDefinition?): List<Suit> {
        return when (cardDefinition) {
            bookOfChanges -> listOfNotNull(bookOfChangeSelection?.second)
            else -> emptyList()
        }
    }

    fun ruleEffectCandidateAbout(cardDefinition: CardDefinition?): List<CardDefinition> {
        return when (cardDefinition) {
            doppelganger -> doppelgangerCandidates()
            mirage -> mirageCandidates()
            shapeshifter -> shapeShifterCandidates()
            bookOfChanges -> bookOfChangesCandidates()
            island -> islandCandidates()
            else -> emptyList()
        }
    }

    fun ruleEffectSelectionMode(cardDefinition: CardDefinition?): Int {
        return when (cardDefinition) {
            doppelganger -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            mirage -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            shapeshifter -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            bookOfChanges -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
            island -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            else -> Constants.CARD_LIST_SELECTION_MODE_DEFAULT
        }
    }

    fun hasManualEffect(definition: CardDefinition): Boolean =
        listOf(doppelganger, mirage, shapeshifter, bookOfChanges, island).contains(definition)

}
