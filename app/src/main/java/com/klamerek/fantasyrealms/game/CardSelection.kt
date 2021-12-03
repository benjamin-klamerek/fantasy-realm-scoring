package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.util.Constants

/**
 * Base class to model cards with selection logic (choosing another card to update)
 */
abstract class CardSelection(val source: CardDefinition) {
    var cardSelected: CardDefinition? = null

    /**
     * Indicates the selection mode for UI (many cards, single card, with Suit, ...)
     */
    open fun selectionMode(): Int = Constants.CARD_LIST_SELECTION_MODE_ONE_CARD

    /**
     * Clear selection if card removed if the source of selected card
     */
    fun clear(toRemove: CardDefinition) {
        if (toRemove == source || toRemove == cardSelected) {
            clear()
        }
    }

    /**
     * Clear selection
     */
    open fun clear(){
        cardSelected = null
    }

    /**
     * Apply card selection effect to the game (like update card type or rule)
     */
    abstract fun apply(game: Game)

    /**
     * Give a list of cards on which this update logic can be applied
     */
    abstract fun candidates(game: Game): List<CardDefinition>

}

/**
 * Base class to model cards with selection logic (choosing another card and Suit to update)
 */
abstract class CardAndSuitSelection(source: CardDefinition) : CardSelection(source) {
    var suitSelected: Suit? = null

    override fun selectionMode(): Int = Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT

    override fun clear() {
        super.clear()
        suitSelected = null
    }

}

class DoppelgangerSelection : CardSelection(doppelganger) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards()
                    .filter { card -> card.definition == source }
                    .map { card ->
                        card.name(it.name())
                        card.suit(it.suit)
                        card.value(it.value)
                        card.rules(
                            AllRules.instance[it].orEmpty()
                                .filter { rule -> rule.tags.contains(Effect.PENALTY) })
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        game.handCards().map { card -> card.definition }
            .filter { definition -> definition != source }

}

class IslandSelection : CardSelection(island) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards().filter { card -> card.definition == it }
                    .map { card ->
                        card.rules()
                            .filter { rule -> rule.tags.contains(Effect.PENALTY) }
                            .forEach { rule -> card.deactivate(rule) }
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        game.handCards().filter { card -> card.isOneOf(Suit.FLOOD, Suit.FLAME) }
            .map { card -> card.definition }

}

class ShapeShifterSelection : CardSelection(shapeshifter) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards().filter { card -> card.definition == shapeshifter }
                    .map { card ->
                        card.name(it.name())
                        card.suit(it.suit)
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        CardDefinitions.get(
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

class ShapeShifterV2Selection : CardSelection(shapeshifterV2) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards().filter { card -> card.definition == shapeshifterV2 }
                    .map { card ->
                        card.name(it.name())
                        card.suit(it.suit)
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        CardDefinitions.get(
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

class MirageSelection : CardSelection(mirage) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards().filter { card -> card.definition == mirage }
                    .map { card ->
                        card.name(it.name())
                        card.suit(it.suit)
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        CardDefinitions.get(
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

class MirageV2Selection : CardSelection(mirageV2) {

    override fun apply(game: Game) {
        cardSelected?.let {
            if (candidates(game).contains(it)) {
                game.handCards().filter { card -> card.definition == mirageV2 }
                    .map { card ->
                        card.name(it.name())
                        card.suit(it.suit)
                    }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        CardDefinitions.get(
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

class AngelSelection : CardSelection(angel) {

    override fun apply(game: Game) {
        cardSelected?.let {
            game.handCards().filter { card -> card.definition == it }
                .map { card -> card.addTemporaryRule(unblankable) }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        game.handCards().filter { card -> card.definition != angel }
            .map { card -> card.definition }

}

class BookOfChangesSelection : CardAndSuitSelection(bookOfChanges) {

    override fun apply(game: Game) {
        cardSelected?.let { cardSelected ->
            suitSelected?.let { suitSelected ->
                game.handCards().filter { card -> card.definition == cardSelected }
                    .map { card -> card.suit(suitSelected) }
            }
        }
    }

    override fun candidates(game: Game): List<CardDefinition> =
        game.handCards().map { card -> card.definition }
            .filter { definition -> definition != bookOfChanges }

}


