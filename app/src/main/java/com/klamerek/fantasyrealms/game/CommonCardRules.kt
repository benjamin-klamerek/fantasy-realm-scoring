package com.klamerek.fantasyrealms.game

import then
import java.lang.Integer.max

/**
 * Most highest class defined a game rule<br>
 * Please note that a rule log should never update object state. A rule must preserve immutability.
 *
 * @param T             rule type
 * @property tags       tags used to identify the kind of rule (bonus, effect and cards, ...)
 * @property logic      game rule logic
 */
open class Rule<T>(val tags: List<Tag>, val logic: (Game) -> T)

/**
 * Rule about score (bonus and penalty)
 *
 */
class RuleAboutScore(tags: List<Tag>, logic: (Game) -> Int) : Rule<Int>(tags, logic)

/**
 * Rule about rule (like deactivate other rules)
 */
class RuleAboutRule(tags: List<Tag>, logic: (Game) -> List<Rule<out Any>>) : Rule<List<Rule<out Any>>>(tags, logic)

/**
 * Rule about cards (likeblank some cards)
 */
class RuleAboutCard(tags: List<Tag>, logic: (Game) -> List<Card>) : Rule<List<Card>>(tags, logic)

/**
 * Contains all common rules of the game (expect the one requesting an external action)
 *
 */
class AllRules {

    companion object {
        val instance = mapOf(
            rangers to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.countCard(Suit.LAND) * 10 },
                RuleAboutRule(listOf(Effect.CLEAR, Suit.ARMY)) { it.identifyArmyClearedPenalty { true } }),
            elvenArchers to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.noCard(Suit.LEADER) then { 5 } ?: 0 }),
            dwarvishInfantry to listOf(
                RuleAboutScore(listOf(Effect.PENALTY, Suit.ARMY)) { it.countCard(Suit.ARMY) * -2 }),
            lightCavalry to listOf(
                RuleAboutScore(listOf(Effect.PENALTY, Suit.LAND)) { it.countCard(Suit.LAND) * -2 }),
            celestialKnights to listOf(
                RuleAboutScore(listOf(Effect.PENALTY, Suit.LEADER)) { it.noCard(Suit.LEADER) then { -8 } ?: 0 }),
            protectionRune to listOf(
                RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyClearedPenalty { true } }),
            worldTree to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    if (it.cardsNotBlanked().groupBy { card -> card.suit() }.entries.any { entry -> entry.value.size > 1 }) 0 else 50
                }),
            shieldOfKeth to listOf(
                RuleAboutScore(listOf(Effect.BONUS, Suit.LEADER)) { it.atLeastOne(Suit.LEADER) then { 15 } ?: 0 },
                RuleAboutScore(listOf(Effect.BONUS, Suit.LEADER)) { (it.atLeastOne(Suit.LEADER) && it.contains(swordOfKeth)) then { 25 } ?: 0 }
            ),
            gemOfOrder to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    mapOf(3 to 10, 4 to 30, 5 to 60, 6 to 100, 7 to 150, 8 to 150)[it.longestSuite()] ?: 0
                }
            ),
            warhorse to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.LEADER) + it.countCard(Suit.WIZARD) > 1) then { 14 } ?: 0 }
            ),
            unicorn to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    if (it.contains(princess)) 30 else if (it.contains(empress) || it.contains(queen) || it.contains(elementalEnchantress)) 15 else 0
                }
            ),
            hydra to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.contains(swamp)) then { 28 } ?: 0 }
            ),
            dragon to listOf(
                RuleAboutScore(listOf(Effect.PENALTY)) { (it.noCard(Suit.WIZARD)) then { -40 } ?: 0 }
            ),
            basilisk to listOf(
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.ARMY))
                { it.identifyBlankedCards { card -> card.isOneOf(Suit.ARMY) } },
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.LEADER))
                { it.identifyBlankedCards { card -> card.isOneOf(Suit.LEADER) } },
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.BEAST))
                { it.identifyBlankedCards { card -> (card.isOneOf(Suit.BEAST) && card.definition != basilisk) } }
            ),
            candle to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.atLeastOne(Suit.WIZARD) && it.contains(bookOfChanges, bellTower)) then { 100 } ?: 0 }
            ),
            fireElemental to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.FLAME) - 1) * 15 }
            ),
            forge to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.WEAPON) + it.countCard(Suit.ARTIFACT)) * 9 }
            ),
            lightning to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.contains(rainstorm) then { 30 } ?: 0 }
            ),
            wildfire to listOf(
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK))
                {
                    it.identifyBlankedCards { card ->
                        !(card.isOneOf(Suit.FLAME) || card.isOneOf(Suit.WEATHER) || card.isOneOf(Suit.WIZARD) ||
                                card.isOneOf(Suit.WEAPON) || card.isOneOf(Suit.ARTIFACT) ||
                                card.definition == greatFlood || card.definition == island ||
                                card.definition == mountain || card.definition == unicorn || card.definition == dragon)
                    }
                }
            ),
            fountainOfLife to listOf(
                RuleAboutScore(listOf(Effect.BONUS))
                {
                    it.cardsNotBlanked().filter { card ->
                        card.isOneOf(Suit.WEAPON, Suit.FLOOD, Suit.FLAME, Suit.LAND, Suit.WEATHER)
                    }.sumBy { card -> card.value() }
                }
            ),
            waterElemental to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.FLOOD) - 1) * 15 }
            ),
            swamp to listOf(RuleAboutScore(listOf(Effect.PENALTY)) { (it.countCard(Suit.ARMY) + it.countCard(Suit.FLAME)) * -3 }),
            greatFlood to listOf(
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.ARMY)) { it.identifyBlankedCards { card -> card.isOneOf(Suit.ARMY) } },
                RuleAboutCard(
                    listOf(
                        Effect.PENALTY,
                        Effect.BLANK,
                        Suit.LAND
                    )
                ) { it.identifyBlankedCards { card -> (card.isOneOf(Suit.LAND) && card.definition != mountain) } },
                RuleAboutCard(
                    listOf(
                        Effect.PENALTY,
                        Effect.BLANK,
                        Suit.FLAME
                    )
                ) { it.identifyBlankedCards { card -> (card.isOneOf(Suit.FLAME) && card.definition != lightning) } }
            ),
            earthElemental to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.LAND) - 1) * 15 }
            ),
            undergroundCaverns to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.contains(dwarvishInfantry) || it.contains(dragon)) then { 25 } ?: 0 },
                RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyClearedPenalty { card -> card.isOneOf(Suit.WEATHER) } }
            ),
            forest to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    (it.countCard(Suit.BEAST) + it.cardsNotBlanked().filter { card -> card.definition == elvenArchers }.count()) * 12
                }
            ),
            bellTower to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.atLeastOne(Suit.WIZARD) then { 15 } ?: 0 }
            ),
            mountain to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.contains(smoke, wildfire) then { 50 } ?: 0 },
                RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyClearedPenalty { card -> card.isOneOf(Suit.FLOOD) } }
            ),
            princess to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    (it.countCard(Suit.ARMY) + it.countCard(Suit.WIZARD) + max(it.countCard(Suit.LEADER) - 1, 0)) * 8
                }
            ),
            warlord to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    it.cardsNotBlanked().filter { card -> card.isOneOf(Suit.ARMY) }.sumBy(Card::value)
                }
            ),
            queen to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.countCard(Suit.ARMY) * 5 },
                RuleAboutScore(listOf(Effect.BONUS)) { (it.contains(king)) then { it.countCard(Suit.ARMY) * 15 } ?: 0 }
            ),
            king to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.countCard(Suit.ARMY) * 5 },
                RuleAboutScore(listOf(Effect.BONUS)) { (it.contains(queen)) then { it.countCard(Suit.ARMY) * 15 } ?: 0 }
            ),
            empress to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.countCard(Suit.ARMY) * 10 },
                RuleAboutScore(listOf(Effect.PENALTY)) { (it.countCard(Suit.LEADER) - 1) * -5 }
            ),
            magicWand to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.atLeastOne(Suit.WIZARD)) then { 25 } ?: 0 }
            ),
            elvenLongbow to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    (it.contains(elvenArchers) || it.contains(warlord) || it.contains(beastmaster)) then { 30 } ?: 0
                }
            ),
            swordOfKeth to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.atLeastOne(Suit.LEADER)) then { 10 } ?: 0 },
                RuleAboutScore(listOf(Effect.BONUS)) { (it.atLeastOne(Suit.LEADER) && it.contains(shieldOfKeth)) then { 30 } ?: 0 }
            ),
            warship to listOf(
                RuleAboutCard(
                    listOf(
                        Effect.PENALTY,
                        Effect.BLANK
                    )
                ) { (it.noCard(Suit.FLOOD)) then { it.identifyBlankedCards { card -> card.definition == warship } } ?: emptyList() },
                RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyArmyClearedPenalty { card -> card.isOneOf(Suit.FLOOD) } }
            ),
            warDirigible to listOf(
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.ARMY))
                { (it.noCard(Suit.ARMY)) then { it.identifyBlankedCards { card -> card.definition == warDirigible } } ?: emptyList() },
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK))
                { (it.atLeastOne(Suit.WEATHER)) then { it.identifyBlankedCards { card -> card.definition == warDirigible } } ?: emptyList() }
            ),
            airElemental to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.WEATHER) - 1) * 15 }
            ),
            rainstorm to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { (it.countCard(Suit.FLOOD)) * 10 },
                RuleAboutCard(
                    listOf(
                        Effect.PENALTY,
                        Effect.BLANK
                    )
                ) { it.identifyBlankedCards { card -> card.isOneOf(Suit.FLAME) && card.definition != lightning } }
            ),
            whirlwind to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) {
                    (it.contains(rainstorm) && (it.contains(blizzard) || it.contains(greatFlood))) then { 40 } ?: 0
                }
            ),
            smoke to listOf(
                RuleAboutCard(
                    listOf(
                        Effect.PENALTY,
                        Effect.BLANK
                    )
                ) { (it.noCard(Suit.FLAME)) then { it.identifyBlankedCards { card -> card.definition == smoke } } ?: emptyList() }
            ),
            blizzard to listOf(
                RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK)) { it.identifyBlankedCards { card -> card.isOneOf(Suit.FLOOD) } },
                RuleAboutScore(listOf(Effect.PENALTY, Suit.ARMY)) { it.countCard(Suit.ARMY) * -5 },
                RuleAboutScore(listOf(Effect.PENALTY, Suit.LEADER)) { it.countCard(Suit.LEADER) * -5 },
                RuleAboutScore(listOf(Effect.PENALTY, Suit.BEAST)) { it.countCard(Suit.BEAST) * -5 },
                RuleAboutScore(listOf(Effect.PENALTY, Suit.FLAME)) { it.countCard(Suit.FLAME) * -5 }
            ),
            elementalEnchantress to listOf(
                RuleAboutScore(listOf(Effect.BONUS))
                { (it.countCard(Suit.LAND) + it.countCard(Suit.WEATHER) + it.countCard(Suit.FLOOD) + it.countCard(Suit.FLAME)) * 5 }
            ),
            collector to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { mapOf(3 to 10, 4 to 40, 5 to 100, 6 to 100, 7 to 100, 8 to 100)[it.largestSuit()] ?: 0 }
            ),
            beastmaster to listOf(
                RuleAboutScore(listOf(Effect.BONUS)) { it.countCard(Suit.BEAST) * 9 },
                RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyClearedPenalty { card -> card.isOneOf(Suit.BEAST) } }
            ),
            warlockLord to listOf(
                RuleAboutScore(listOf(Effect.PENALTY)) { (it.countCard(Suit.LEADER) + max(it.countCard(Suit.WIZARD) - 1, 0)) * -10 }
            )
        )
    }

}


