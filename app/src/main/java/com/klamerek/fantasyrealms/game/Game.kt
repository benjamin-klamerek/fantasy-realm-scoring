package com.klamerek.fantasyrealms.game

import android.content.Context
import com.klamerek.fantasyrealms.*
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Constants.MAX_HAND_SIZE
import com.klamerek.fantasyrealms.util.Preferences
import java.lang.Integer.max
import kotlin.math.min

/**
 * List of cards (player hand) wth scoring calculation
 *
 */
class Game(val noScoring: Boolean = false) {

    private val handCards = ArrayList<Card>()
    private val tableCards = ArrayList<Card>()
    private val bonusScoreByCard = HashMap<CardDefinition, Int>()
    private val penaltyScoreByCard = HashMap<CardDefinition, Int>()

    private val selections = listOf(
        DoppelgangerSelection(), IslandSelection(),
        ShapeShifterSelection(), ShapeShifterV2Selection(), MirageSelection(),
        MirageV2Selection(), AngelSelection(), BookOfChangesSelection()
    ).map { selection -> selection.source to selection }.toMap()

    fun cards(): Collection<Card> {
        return handCards.plus(tableCards)
    }

    fun handCards(): Collection<Card> {
        return ArrayList(handCards)
    }

    fun handCardsNotBlanked(): Collection<Card> {
        return handCards.filter { card -> !card.blanked || card.definition == phoenix }
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
        selections.values.forEach { it.clear(cardDefinition) }
    }

    fun update(cardDefinitions: List<CardDefinition>) {
        val cardsToRemove = ArrayList<CardDefinition>()
        listOf(handCards, tableCards).forEach {
            it.forEach { card ->
                if (!cardDefinitions.contains(card.definition)) {
                    cardsToRemove.add(card.definition)
                }
            }
        }
        cardsToRemove.forEach { remove(it) }
        addAll(cardDefinitions)
    }

    fun addAll(cardDefinitions: List<CardDefinition>) {
        cardDefinitions.forEach { definition -> add(definition) }
    }

    fun clear() {
        selections.values.forEach { it.clear() }
        handCards.forEach { it.clear() }
        handCards.clear()
        tableCards.clear()
    }

    fun countOddHandCards() = handCardsNotBlanked().count { it.isOdd() }

    fun isAllHandCardsOdd() = handCardsNotBlanked().all { it.isOdd() }

    fun countHandCards(vararg suit: Suit) = handCardsNotBlanked().count { it.isOneOf(*suit) }

    fun countHandCardsByName(definition: CardDefinition) =
        handCardsNotBlanked().count { it.hasSameNameThan(definition) }

    fun countHandCardsExcept(suit: Suit, cardDefinition: CardDefinition) =
        handCardsNotBlanked().filter { it.definition != cardDefinition }.count { it.isOneOf(suit) }

    fun countHandCardsExcept(suits: List<Suit>, cardDefinition: CardDefinition) =
        handCardsNotBlanked().filter { it.definition != cardDefinition }.count { it.isOneOf(*suits.toTypedArray()) }

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

    fun largestSuitWithDifferentNames(): Int {
        return groupNotBlankedCardsBySuit()
            .map { entry -> entry.key to entry.value.distinct() }.toMap()
            .map { it.value.size }.maxOrNull() ?: 0
    }

    fun calculate() {
        handCards.forEach { card -> card.clear() }

        if (!noScoring) {
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

            handCards.map { card -> identifyReduceBaseStrengthToZeroCards(card) }.flatten()
                .forEach { card -> card.value(0) }

            bonusScoreByCard.clear()
            penaltyScoreByCard.clear()
            bonusScoreByCard.putAll(cardsForScoring().map { card ->
                card.definition to card.rules()
                    .asSequence()
                    .activated(card)
                    .asRuleAboutScore()
                    .with(Effect.BONUS)
                    .score(this)
            }.toMap())
            penaltyScoreByCard.putAll(cardsForScoring().map { card ->
                card.definition to card.rules()
                    .asSequence()
                    .activated(card)
                    .asRuleAboutScore()
                    .with(Effect.PENALTY)
                    .score(this)
            }.toMap())
        }

    }

    private fun applySpecificCardEffects() {
        selections.values.forEach { it.apply(this) }
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
     * - We apply each BLANK rule by priority order (introduced with Demon card and finish with self blank rules)<br>
     * - Second ordering criteria is "if card is blanked by other cards"
     * - We take attention before applying the rule that the card is still active.<br>
     * - Some cards are UNBLANKABLE (like Angel).<br>
     */
    private fun applyBlankingRules() {
        val cardsPotentiallyBlanked = handCards.map { card ->
            card.rules().activated(card)
                .with(Effect.BLANK).asRuleAboutCard().listCards(this)
        }.flatten()

        handCards.map { card ->
            card.rules()
                .activated(card)
                .with(Effect.BLANK)
                .asRuleAboutCard()
                .map { rule -> Pair(card, rule) }
        }.flatten()
            .sortedWith(
                compareBy({ pair -> pair.second?.priority?.unaryMinus() },
                    { pair -> cardsPotentiallyBlanked.contains(pair.first) })
            )
            .forEach() { if (!it.first.blanked) applyBlankingRule(it.second) }
    }

    private fun applyBlankingRule(rule: RuleAboutCard?) {
        if (rule?.tags?.contains(Effect.BLANK) == true) {
            rule.logic.invoke(this)
                .filter { potentialCard -> !potentialCard.rules().contains(unblankable) }
                .forEach { cardToBlank ->
                    cardToBlank.blanked = true
                }
        }
    }

    private fun identifyClearedRules(card: Card): List<Rule<out Any>> =
        card.rules().activated(card).with(Effect.CLEAR)
            .asRuleAboutRule().listRules(this)

    private fun identifyUnblankableCards(card: Card): List<Card> =
        card.rules().activated(card).with(Effect.UNBLANKABLE)
            .asRuleAboutCard().listCards(this)


    private fun identifyReduceBaseStrengthToZeroCards(card: Card): List<Card> =
        card.rules().activated(card).with(Effect.REDUCE_BASE_STRENGTH_TO_ZERO)
            .asRuleAboutCard().listCards(this)


    fun filterNotBlankedHandCards(scope: (Card) -> Boolean): List<Card> {
        return this.handCardsNotBlanked().filter(scope)
    }

    fun identifyPenalties(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return handCardsNotBlanked().filter(scope).rules().with(Effect.PENALTY)
    }

    fun identifyArmyPenalties(scope: (Card) -> Boolean): List<Rule<out Any>> {
        return handCardsNotBlanked().filter(scope).rules().with(Effect.PENALTY).with(Suit.ARMY)
    }

    fun handSizeExpected(context: Context): Int {
        return min(
            MAX_HAND_SIZE, Constants.DEFAULT_HAND_SIZE +
                    Preferences.getBuildingsOutsidersUndead(context).toInt() +
                    handCards.any { card ->
                        card.definition == necromancer || card.definition == necromancerV2
                    }.toInt() +
                    handCards.any { card -> card.definition == genie }.toInt() +
                    handCards.any { card -> card.definition == leprechaun }.toInt()
        )
    }

    fun actualHandSize(): Int {
        return handCards.size
    }

    fun ruleEffectCardSelectionAbout(cardDefinition: CardDefinition?): List<CardDefinition> =
        listOfNotNull(selections[cardDefinition]?.cardSelected)

    fun ruleEffectSuitSelectionAbout(cardDefinition: CardDefinition?): List<Suit> =
        listOfNotNull((selections[cardDefinition] as? CardAndSuitSelection)?.suitSelected)

    fun ruleEffectCandidateAbout(cardDefinition: CardDefinition?): List<CardDefinition> =
        selections[cardDefinition]?.candidates(this) ?: emptyList()

    fun ruleEffectSelectionMode(cardDefinition: CardDefinition?): Int =
        selections[cardDefinition]?.selectionMode() ?: Constants.CARD_LIST_SELECTION_MODE_DEFAULT

    fun hasManualEffect(definition: CardDefinition): Boolean =
        selections.keys.contains(definition)

    fun countCardWithAtLeastOnePenaltyNotCleared(): Int {
        return handCardsNotBlanked().count { card ->
            card.rules().activated(card).with(Effect.PENALTY).any()
        }
    }

    fun groupNotBlankedCardsBySuit(): Map<Suit, List<Card>> {
        return handCardsNotBlanked()
            .flatMap { card ->
                listOf(card.suit()).plus(card.definition.additionalSuits).map { card to it }
            }
            .groupBy({ it.second }, { it.first })
    }

    fun applySelection(
        cardDefinition: CardDefinition?,
        cardSelected: CardDefinition?,
    ) {
        selections[cardDefinition]?.cardSelected = cardSelected
    }

    fun applySelection(
        cardDefinition: CardDefinition?,
        cardSelected: CardDefinition?,
        suitSelected: Suit?
    ) {
        applySelection(cardDefinition, cardSelected)
        (selections[cardDefinition] as? CardAndSuitSelection)?.suitSelected = suitSelected
    }

}
