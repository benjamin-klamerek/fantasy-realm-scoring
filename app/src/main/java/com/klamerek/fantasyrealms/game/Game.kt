package com.klamerek.fantasyrealms.game

import android.content.Context
import com.klamerek.fantasyrealms.toInt
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Preferences
import java.lang.Integer.max

/**
 * List of cards (player hand) wth scoring calculation
 *
 */
class Game {

    private val handCards = ArrayList<Card>()
    private val tableCards = ArrayList<Card>()
    private val bonusScoreByCard = HashMap<CardDefinition, Int>()
    private val penaltyScoreByCard = HashMap<CardDefinition, Int>()

    var bookOfChangeSelection: Pair<CardDefinition?, Suit?>? = null
    var islandSelection: CardDefinition? = null
    var shapeShifterSelection: CardDefinition? = null
    var shapeShifterV2Selection: CardDefinition? = null
    var mirageSelection: CardDefinition? = null
    var mirageV2Selection: CardDefinition? = null
    var doppelgangerSelection: CardDefinition? = null
    var angelSelection: CardDefinition? = null

    fun cards(): Collection<Card> {
        return handCards.plus(tableCards)
    }

    fun handCards(): Collection<Card> {
        return ArrayList(handCards)
    }

    fun handCardsNotBlanked(): Collection<Card> {
        return handCards.filter { card -> !card.blanked }
    }

    fun add(cardDefinition: CardDefinition) {
        val list = if (cardDefinition.position() == CardPosition.HAND) handCards else tableCards
        if (!list.map { card -> card.definition }.contains(cardDefinition)) {
            list.add(Card(cardDefinition, AllRules.instance[cardDefinition].orEmpty()))
        }
    }

    fun remove(cardDefinition: CardDefinition) {
        val list = if (cardDefinition.position() == CardPosition.HAND) handCards else tableCards
        list.removeIf { it.definition == cardDefinition }
        if (cardDefinition == bookOfChanges || cardDefinition == bookOfChangeSelection?.first) {
            bookOfChangeSelection = null
        }
        if (cardDefinition == island || cardDefinition == islandSelection) {
            islandSelection = null
        }
        if (cardDefinition == shapeshifter || cardDefinition == shapeShifterSelection) {
            shapeShifterSelection = null
        }
        if (cardDefinition == shapeshifterV2 || cardDefinition == shapeShifterV2Selection) {
            shapeShifterV2Selection = null
        }
        if (cardDefinition == mirage || cardDefinition == mirageSelection) {
            mirageSelection = null
        }
        if (cardDefinition == mirageV2 || cardDefinition == mirageV2Selection) {
            mirageV2Selection = null
        }
        if (cardDefinition == doppelganger || cardDefinition == doppelgangerSelection) {
            doppelgangerSelection = null
        }
        if (cardDefinition == angel || cardDefinition == angelSelection) {
            angelSelection = null
        }
    }

    fun update(cardDefinitions: List<CardDefinition>) {
        cardDefinitions.forEach { definition -> add(definition) }
        handCards.plus(tableCards).map { card -> card.definition }
            .filter { definition -> !cardDefinitions.contains(definition) }
            .forEach { definition -> remove(definition) }
    }

    fun clear() {
        handCards.clear()
        tableCards.clear()
    }

    fun countOddHandCards() = handCardsNotBlanked().count { it.isOdd() }

    fun isAllHandCardsOdd() = handCardsNotBlanked().all { it.isOdd() }

    fun countHandCards(vararg suit: Suit) = handCardsNotBlanked().count { it.isOneOf(*suit) }

    fun countHandCardsExcept(suit: Suit, cardDefinition: CardDefinition) =
        handCardsNotBlanked().filter { it.definition != cardDefinition }.count { it.isOneOf(suit) }

    fun countTableCards(vararg suit: Suit) = tableCards.count { it.isOneOf(*suit) }

    fun noHandCardsOf(suit: Suit) = countHandCards(suit) == 0

    fun atLeastOneHandCardOf(suit: Suit) = countHandCards(suit) > 0


    /**
     * Remark : this method uses comparison by name to handle when a wild card simulate another card.
     *
     * @param cardExpected      list of cards expected in the game
     */
    fun containsHandCards(vararg cardExpected: CardDefinition) = handCardsNotBlanked()
        .map { it.name() }.containsAll(cardExpected.toList().map { it.name() })

    fun longestSuite(): Int {
        val sorted = handCardsNotBlanked().sortedBy { card -> card.value() }
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
        return groupNotBlankedCardsBySuit().map { entry -> entry.value.size }.maxOrNull() ?: 0
    }

    fun calculate() {
        handCards.forEach { card -> card.clear() }

        applySpecificCardEffects()

        handCards.map { card -> identifyClearedRules(card) }.flatten()
            .forEach { ruleToDeactivate ->
                handCards.forEach { card ->
                    card.rules()
                        .filter { rule -> rule == ruleToDeactivate }
                        .forEach { rule -> card.deactivate(rule) }
                }
            }

        handCards.map { card -> identifyUnblankableCards(card) }.flatten()
            .forEach { card -> card.addTemporaryRule(unblankable) }

        applyBlankingRules()

        bonusScoreByCard.clear()
        penaltyScoreByCard.clear()
        bonusScoreByCard.putAll(cardsForScoring().map { card ->
            card.definition to card.rules()
                .asSequence()
                .filter { rule -> card.isActivated(rule) }
                .map { rule -> rule as? RuleAboutScore }
                .filter { rule -> rule?.tags?.contains(Effect.BONUS) ?: false }
                .map { rule -> rule?.logic?.invoke(this) }
                .sumOf { any -> if (any is Int) any else 0 }
        }.toMap())
        penaltyScoreByCard.putAll(cardsForScoring().map { card ->
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
        mirageV2Selection?.let { cardDefinition -> applyMirageV2(cardDefinition) }
        shapeShifterSelection?.let { cardDefinition -> applyShapeShifter(cardDefinition) }
        shapeShifterV2Selection?.let { cardDefinition -> applyShapeShifterV2(cardDefinition) }
        bookOfChangeSelection?.let { pair -> applyBookOfChanges(pair.first, pair.second) }
        islandSelection?.let { cardDefinition -> applyIsland(cardDefinition) }
        angelSelection?.let { cardDefinition -> applyAngel(cardDefinition) }
    }

    fun score(): Int = bonusScoreByCard.entries.sumOf { it.value } +
            penaltyScoreByCard.entries.sumOf { it.value } +
            cardsForScoring().sumOf { it.value() }

    fun score(card: CardDefinition): Int = bonusScore(card) +
            penaltyScore(card) +
            cardsForScoring().filter { it.definition == card }.sumOf { it.value() }

    private fun cardsForScoring() = handCardsNotBlanked().plus(tableCards)

    fun bonusScore(card: CardDefinition): Int = bonusScoreByCard.getOrDefault(card, 0)

    fun penaltyScore(card: CardDefinition): Int = penaltyScoreByCard.getOrDefault(card, 0)

    /**
     * Sensitive method. Apply BLANK rules on cards<br>
     * - We apply each BLANK rule by priority order (introduced with Demon card)<br>
     * - We take attention before applying the rule that the card is still active.<br>
     * - Some cards are UNBLANKABLE (like Angel).<br>
     */
    private fun applyBlankingRules() {
        handCards.map { card ->
            card.rules()
                .filter { rule -> card.isActivated(rule) }
                .filter { rule -> rule.tags.contains(Effect.BLANK) }
                .map { rule -> rule as? RuleAboutCard }
                .map { rule -> Pair(card, rule) }
        }.flatten()
            .sortedByDescending { pair -> pair.second?.priority }
            .forEach() { applyBlankingRules(it) }
    }

    private fun applyBlankingRules(pair: Pair<Card, RuleAboutCard?>) {
        if (!pair.first.blanked) {
            pair.second?.logic?.invoke(this)
                ?.filter { potentialCard -> !potentialCard.rules().contains(unblankable) }
                .orEmpty().forEach { cardToBlank ->
                    cardToBlank.blanked = true
                }
        }
    }

    private fun identifyClearedRules(card: Card): List<Rule<out Any>> =
        card.rules().filter { rule -> card.isActivated(rule) }
            .filter { rule -> rule.tags.contains(Effect.CLEAR) }
            .map { rule -> rule as? RuleAboutRule }
            .flatMap { rule -> rule?.logic?.invoke(this).orEmpty() }

    private fun identifyUnblankableCards(card: Card): List<Card> =
        card.rules().filter { rule -> card.isActivated(rule) }
            .filter { rule -> rule.tags.contains(Effect.UNBLANKABLE) }
            .map { rule -> rule as? RuleAboutCard }
            .flatMap { rule -> rule?.logic?.invoke(this).orEmpty() }


    fun filterNotBlankedHandCards(scope: (Card) -> Boolean): List<Card> {
        return this.handCardsNotBlanked().filter(scope)
    }

    fun identifyClearedPenalty(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return identifyRule(scope, Effect.PENALTY)
    }

    fun identifyArmyClearedPenalty(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return identifyRule(scope, Effect.PENALTY, Suit.ARMY)
    }

    private fun identifyRule(scope: (Card) -> Boolean, vararg tags: Tag): List<Rule<out Any>> {
        return handCardsNotBlanked().filter(scope)
            .flatMap { card -> card.rules() }
            .filter { rule -> rule.tags.containsAll(tags.asList()) }
    }

    private fun bookOfChangesCandidates(): List<CardDefinition> {
        return handCards.map { card -> card.definition }
            .filter { definition -> definition != bookOfChanges }
    }

    private fun applyBookOfChanges(cardToUpdate: CardDefinition?, newSuit: Suit?) =
        handCards.filter { card -> card.definition == cardToUpdate }
            .map { card -> card.suit(newSuit) }

    private fun islandCandidates(): List<CardDefinition> {
        return handCards.filter { card -> card.isOneOf(Suit.FLOOD, Suit.FLAME) }
            .map { card -> card.definition }
    }

    private fun applyIsland(cardToClean: CardDefinition) {
        if (islandCandidates().contains(cardToClean)) {
            handCards.filter { card -> card.definition == cardToClean }
                .map { card ->
                    card.rules()
                        .filter { rule -> rule.tags.contains(Effect.PENALTY) }
                        .forEach { rule -> card.deactivate(rule) }
                }
        }
    }

    private fun shapeShifterCandidates(): List<CardDefinition> {
        return CardDefinitions.get(
            buildingsOutsidersUndead = false,
            cursedItems = false
        ).filter { definition ->
            definition.isOneOf(
                Suit.ARTIFACT,
                Suit.LEADER,
                Suit.WIZARD,
                Suit.WEAPON,
                Suit.BEAST
            )
        }
    }

    private fun shapeShifterV2Candidates(): List<CardDefinition> {
        return CardDefinitions.get(
            buildingsOutsidersUndead = true,
            cursedItems = false
        ).filter { definition ->
            definition.isOneOf(
                Suit.ARTIFACT,
                Suit.LEADER,
                Suit.WIZARD,
                Suit.WEAPON,
                Suit.BEAST,
                Suit.UNDEAD
            )
        }
    }

    private fun applyShapeShifter(cardToCopy: CardDefinition) {
        if (shapeShifterCandidates().contains(cardToCopy)) {
            handCards.filter { card -> card.definition == shapeshifter }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    private fun applyShapeShifterV2(cardToCopy: CardDefinition) {
        if (shapeShifterV2Candidates().contains(cardToCopy)) {
            handCards.filter { card -> card.definition == shapeshifterV2 }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    private fun angelCandidates(): List<CardDefinition> {
        return handCards.filter { card -> card.definition != angel }
            .map { card -> card.definition }
    }

    private fun applyAngel(cardToProtect: CardDefinition) {
        handCards.filter { card -> card.definition == cardToProtect }
            .map { card -> card.addTemporaryRule(unblankable) }
    }

    private fun mirageCandidates(): List<CardDefinition> {
        return CardDefinitions.get(
            buildingsOutsidersUndead = false,
            cursedItems = false
        ).filter { definition ->
            definition.isOneOf(
                Suit.ARMY,
                Suit.LAND,
                Suit.WEATHER,
                Suit.FLOOD,
                Suit.FLAME
            )
        }
    }

    private fun mirageV2Candidates(): List<CardDefinition> {
        return CardDefinitions.get(
            buildingsOutsidersUndead = true,
            cursedItems = false
        ).filter { definition ->
            definition.isOneOf(
                Suit.ARMY,
                Suit.LAND,
                Suit.WEATHER,
                Suit.FLOOD,
                Suit.FLAME,
                Suit.BUILDING
            )
        }
    }

    private fun applyMirage(cardToCopy: CardDefinition) {
        if (mirageCandidates().contains(cardToCopy)) {
            handCards.filter { card -> card.definition == mirage }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    private fun applyMirageV2(cardToCopy: CardDefinition) {
        if (mirageV2Candidates().contains(cardToCopy)) {
            handCards.filter { card -> card.definition == mirageV2 }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                }
        }
    }

    private fun doppelgangerCandidates(): List<CardDefinition> =
        handCards.map { card -> card.definition }
            .filter { definition -> definition != doppelganger }

    private fun applyDoppelganger(cardToCopy: CardDefinition) {
        if (doppelgangerCandidates().contains(cardToCopy)) {
            handCards.filter { card -> card.definition == doppelganger }
                .map { card ->
                    card.name(cardToCopy.name())
                    card.suit(cardToCopy.suit)
                    card.value(cardToCopy.value)
                    card.rules(
                        AllRules.instance[cardToCopy].orEmpty()
                            .filter { rule -> rule.tags.contains(Effect.PENALTY) })
                }
        }
    }

    fun handSizeExpected(context: Context): Int {
        return Constants.DEFAULT_HAND_SIZE +
                Preferences.getBuildingsOutsidersUndead(context).toInt() +
                handCards.any { card ->
                    card.definition == necromancer || card.definition == necromancerV2
                }.toInt() +
                handCards.any { card -> card.definition == genie }.toInt() +
                handCards.any { card -> card.definition == leprechaun }.toInt()
    }

    fun actualHandSize(): Int {
        return handCards.size
    }

    fun ruleEffectCardSelectionAbout(cardDefinition: CardDefinition?): List<CardDefinition> {
        return when (cardDefinition) {
            doppelganger -> listOfNotNull(doppelgangerSelection)
            mirage -> listOfNotNull(mirageSelection)
            mirageV2 -> listOfNotNull(mirageV2Selection)
            shapeshifter -> listOfNotNull(shapeShifterSelection)
            shapeshifterV2 -> listOfNotNull(shapeShifterV2Selection)
            bookOfChanges -> listOfNotNull(bookOfChangeSelection?.first)
            island -> listOfNotNull(islandSelection)
            angel -> listOfNotNull(angelSelection)
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
            mirageV2 -> mirageV2Candidates()
            shapeshifter -> shapeShifterCandidates()
            shapeshifterV2 -> shapeShifterV2Candidates()
            bookOfChanges -> bookOfChangesCandidates()
            island -> islandCandidates()
            angel -> angelCandidates()
            else -> emptyList()
        }
    }

    fun ruleEffectSelectionMode(cardDefinition: CardDefinition?): Int {
        return when (cardDefinition) {
            doppelganger -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            mirage -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            mirageV2 -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            shapeshifter -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            shapeshifterV2 -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            bookOfChanges -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
            island -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            angel -> Constants.CARD_LIST_SELECTION_MODE_ONE_CARD
            else -> Constants.CARD_LIST_SELECTION_MODE_DEFAULT
        }
    }

    fun hasManualEffect(definition: CardDefinition): Boolean =
        listOf(
            doppelganger,
            mirage,
            mirageV2,
            shapeshifter,
            shapeshifterV2,
            bookOfChanges,
            island,
            angel
        ).contains(definition)

    fun countCardWithAtLeastOnePenaltyNotCleared(): Int {
        return handCardsNotBlanked().count { card ->
            card.rules()
                .any { rule -> rule.tags.contains(Effect.PENALTY) && card.isActivated(rule) }
        }
    }

    fun groupNotBlankedCardsBySuit(): Map<Suit, List<Card>> {
        return handCardsNotBlanked().groupBy { card -> card.suit() }
    }

    fun applySelection(
        cardDefinition: CardDefinition?,
        cardSelected: CardDefinition?,
        suitSelected: Suit?
    ) {
        when (cardDefinition) {
            bookOfChanges -> bookOfChangeSelection = Pair(cardSelected, suitSelected)
            island -> islandSelection = cardSelected
            shapeshifter -> shapeShifterSelection = cardSelected
            shapeshifterV2 -> shapeShifterV2Selection = cardSelected
            mirage -> mirageSelection = cardSelected
            mirageV2 -> mirageV2Selection = cardSelected
            angel -> angelSelection = cardSelected
            doppelganger -> doppelgangerSelection = cardSelected
        }
    }

}
