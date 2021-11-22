package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.toInt
import java.lang.Integer.max

/**
 * Most highest class defined a game rule<br>
 * Please note that a rule should never update object state. A rule must preserve immutability.
 *
 * @param T             rule type
 * @property tags       tags used to identify the kind of rule (bonus, effect and cards, ...)
 * @property priority   priority order used in some cases. Default value is 100, higher value means higher priority.
 * @property logic      game rule logic
 */
open class Rule<T>(val tags: List<Tag>, val priority: Int = 100, val logic: (Game) -> T)

/**
 * Rule about effect (like UNBLANKABLE)
 *
 */
class RuleAboutEffect(tags: List<Tag>, priority: Int = 100, logic: (Game) -> Effect) :
    Rule<Effect>(tags, priority, logic)

val unblankable by lazy {
    RuleAboutEffect(listOf(Effect.BONUS, Effect.UNBLANKABLE)) { Effect.UNBLANKABLE }
}

/**
 * Rule about score (bonus and penalty)
 *
 */
class RuleAboutScore(tags: List<Tag>, priority: Int = 100, logic: (Game) -> Int) :
    Rule<Int>(tags, priority, logic)

/**
 * Rule about rule (like deactivate other rules)
 */
class RuleAboutRule(tags: List<Tag>, priority: Int = 100, logic: (Game) -> List<Rule<out Any>>) :
    Rule<List<Rule<out Any>>>(tags, priority, logic)

/**
 * Rule about cards (like blank some cards)
 */
class RuleAboutCard(tags: List<Tag>, priority: Int = 100, logic: (Game) -> List<Card>) :
    Rule<List<Card>>(tags, priority, logic)

/**
 * Contains all common rules of the game (expect the one requesting an external action)
 *
 */
@Suppress("MagicNumber")
object AllRules {
    val instance = mapOf(
        rangers to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countHandCards(Suit.LAND) * 10 },
            RuleAboutRule(
                listOf(
                    Effect.CLEAR,
                    Suit.ARMY
                )
            ) { it.identifyArmyClearedPenalty { true } }),
        elvenArchers to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.noHandCardsOf(Suit.WEATHER)) 5 else 0 }),
        dwarvishInfantry to listOf(
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.ARMY
                )
            ) { (it.countHandCards(Suit.ARMY) - 1) * -2 }),
        lightCavalry to listOf(
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.LAND
                )
            ) { it.countHandCards(Suit.LAND) * -2 }),
        celestialKnights to listOf(
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.LEADER
                )
            ) { if (it.noHandCardsOf(Suit.LEADER)) -8 else 0 }),
        protectionRune to listOf(
            RuleAboutRule(listOf(Effect.CLEAR)) { it.identifyClearedPenalty { true } }),
        worldTree to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.handCardsNotBlanked()
                        .groupBy { card -> card.suit() }.entries.any { entry -> entry.value.size > 1 }
                ) 0 else 50
            }),
        shieldOfKeth to listOf(
            RuleAboutScore(
                listOf(
                    Effect.BONUS,
                    Suit.LEADER
                )
            ) { if (it.atLeastOneHandCardOf(Suit.LEADER)) 15 else 0 },
            RuleAboutScore(
                listOf(
                    Effect.BONUS,
                    Suit.LEADER
                )
            ) { if (it.atLeastOneHandCardOf(Suit.LEADER) && it.containsHandCards(swordOfKeth)) 25 else 0 }
        ),
        gemOfOrder to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                mapOf(3 to 10, 4 to 30, 5 to 60, 6 to 100, 7 to 150, 8 to 150)[it.longestSuite()]
                    ?: 0
            }
        ),
        warhorse to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.countHandCards(
                        Suit.LEADER,
                        Suit.WIZARD
                    ) >= 1
                ) 14 else 0
            }
        ),
        unicorn to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(princess)) 30 else if (it.containsHandCards(empress) || it.containsHandCards(
                        queen
                    ) || it.containsHandCards(
                        elementalEnchantress
                    )
                ) 15 else 0
            }
        ),
        hydra to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.containsHandCards(swamp)) 28 else 0 }
        ),
        dragon to listOf(
            RuleAboutScore(listOf(Effect.PENALTY)) { if (it.noHandCardsOf(Suit.WIZARD)) -40 else 0 }
        ),
        basilisk to listOf(
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.ARMY))
            { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.ARMY) } },
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.LEADER))
            { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.LEADER) } },
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.BEAST))
            { it.filterNotBlankedHandCards { card -> (card.isOneOf(Suit.BEAST) && card.definition != basilisk) } }
        ),
        candle to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.atLeastOneHandCardOf(Suit.WIZARD) && it.containsHandCards(
                        bookOfChanges,
                        bellTower
                    )
                ) 100 else 0
            }
        ),
        fireElemental to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (it.countHandCards(Suit.FLAME) - 1) * 15 }
        ),
        forge to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(
                    Suit.WEAPON,
                    Suit.ARTIFACT
                )) * 9
            }
        ),
        lightning to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.containsHandCards(rainstorm)) 30 else 0 }
        ),
        wildfire to listOf(
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK))
            {
                it.filterNotBlankedHandCards { card ->
                    !(card.isOneOf(Suit.FLAME) || card.isOneOf(Suit.WEATHER) || card.isOneOf(Suit.WIZARD) ||
                            card.isOneOf(Suit.WEAPON) || card.isOneOf(Suit.ARTIFACT) ||
                            card.hasSameNameThan(greatFlood) ||
                            card.hasSameNameThan(island) ||
                            card.hasSameNameThan(mountain) ||
                            card.hasSameNameThan(unicorn) ||
                            card.hasSameNameThan(dragon))
                }
            }
        ),
        fountainOfLife to listOf(
            RuleAboutScore(listOf(Effect.BONUS))
            {
                it.handCardsNotBlanked().filter { card ->
                    card.isOneOf(Suit.WEAPON, Suit.FLOOD, Suit.FLAME, Suit.LAND, Suit.WEATHER)
                }.maxOfOrNull { card -> card.value() } ?: 0
            }
        ),
        waterElemental to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (it.countHandCards(Suit.FLOOD) - 1) * 15 }
        ),
        swamp to listOf(RuleAboutScore(listOf(Effect.PENALTY)) {
            (it.countHandCards(Suit.ARMY, Suit.FLAME)) * -3
        }),
        greatFlood to listOf(
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.ARMY
                )
            ) { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.ARMY) } },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.LAND
                )
            ) { it.filterNotBlankedHandCards { card -> (card.isOneOf(Suit.LAND) && card.definition != mountain) } },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.FLAME
                )
            ) { it.filterNotBlankedHandCards { card -> (card.isOneOf(Suit.FLAME) && card.definition != lightning) } }
        ),
        earthElemental to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (it.countHandCards(Suit.LAND) - 1) * 15 }
        ),
        undergroundCaverns to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(dwarvishInfantry) || it.containsHandCards(
                        dragon
                    )
                ) 25 else 0
            },
            RuleAboutRule(listOf(Effect.CLEAR)) {
                it.identifyClearedPenalty { card ->
                    card.isOneOf(
                        Suit.WEATHER
                    )
                }
            }
        ),
        forest to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.BEAST) + it.handCardsNotBlanked()
                    .filter { card -> card.hasSameNameThan(elvenArchers) }.count()) * 12
            }
        ),
        bellTower to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.atLeastOneHandCardOf(Suit.WIZARD)) 15 else 0 }
        ),
        mountain to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(
                        smoke,
                        wildfire
                    )
                ) 50 else 0
            },
            RuleAboutRule(listOf(Effect.CLEAR)) {
                it.identifyClearedPenalty { card ->
                    card.isOneOf(
                        Suit.FLOOD
                    )
                }
            }
        ),
        princess to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.ARMY, Suit.WIZARD) +
                        max(it.countHandCards(Suit.LEADER) - 1, 0)) * 8
            }
        ),
        warlord to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                it.handCardsNotBlanked().filter { card -> card.isOneOf(Suit.ARMY) }
                    .sumOf(Card::value)
            }
        ),
        queen to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countHandCards(Suit.ARMY) * 5 },
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(king)) it.countHandCards(
                    Suit.ARMY
                ) * 15 else 0
            }
        ),
        king to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countHandCards(Suit.ARMY) * 5 },
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(queen)) it.countHandCards(
                    Suit.ARMY
                ) * 15 else 0
            }
        ),
        empress to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countHandCards(Suit.ARMY) * 10 },
            RuleAboutScore(listOf(Effect.PENALTY)) { (it.countHandCards(Suit.LEADER) - 1) * -5 }
        ),
        magicWand to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.atLeastOneHandCardOf(Suit.WIZARD)) 25 else 0 }
        ),
        elvenLongbow to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(elvenArchers) || it.containsHandCards(warlord) || it.containsHandCards(
                        beastmaster
                    )
                ) 30 else 0
            }
        ),
        swordOfKeth to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.atLeastOneHandCardOf(Suit.LEADER)) 10 else 0 },
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.atLeastOneHandCardOf(Suit.LEADER) && it.containsHandCards(
                        shieldOfKeth
                    )
                ) 30 else 0
            }
        ),
        warship to listOf(
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK
                )
            ) {
                if (it.noHandCardsOf(Suit.FLOOD)) it.filterNotBlankedHandCards { card ->
                    card.hasSameNameThan(
                        warship
                    )
                } else emptyList()
            },
            RuleAboutRule(listOf(Effect.CLEAR)) {
                it.identifyArmyClearedPenalty { card ->
                    card.isOneOf(
                        Suit.FLOOD
                    )
                }
            }
        ),
        warDirigible to listOf(
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.ARMY))
            {
                if (it.noHandCardsOf(Suit.ARMY)) it.filterNotBlankedHandCards { card ->
                    card.hasSameNameThan(
                        warDirigible
                    )
                } else emptyList()
            },
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK))
            {
                if (it.atLeastOneHandCardOf(Suit.WEATHER)) it.filterNotBlankedHandCards { card ->
                    card.hasSameNameThan(
                        warDirigible
                    )
                } else emptyList()
            }
        ),
        airElemental to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (it.countHandCards(Suit.WEATHER) - 1) * 15 }
        ),
        rainstorm to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (it.countHandCards(Suit.FLOOD)) * 10 },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK
                )
            ) { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.FLAME) && card.definition != lightning } }
        ),
        whirlwind to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.containsHandCards(rainstorm) && (it.containsHandCards(blizzard) || it.containsHandCards(
                        greatFlood
                    ))
                ) 40 else 0
            }
        ),
        smoke to listOf(
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK
                )
            ) {
                if (it.noHandCardsOf(Suit.FLAME)) it.filterNotBlankedHandCards { card ->
                    card.hasSameNameThan(
                        smoke
                    )
                } else emptyList()
            }
        ),
        blizzard to listOf(
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK
                )
            ) { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.FLOOD) } },
            RuleAboutScore(listOf(Effect.PENALTY, Suit.ARMY)) { it.countHandCards(Suit.ARMY) * -5 },
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.LEADER
                )
            ) { it.countHandCards(Suit.LEADER) * -5 },
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.BEAST
                )
            ) { it.countHandCards(Suit.BEAST) * -5 },
            RuleAboutScore(
                listOf(
                    Effect.PENALTY,
                    Suit.FLAME
                )
            ) { it.countHandCards(Suit.FLAME) * -5 }
        ),
        elementalEnchantress to listOf(
            RuleAboutScore(listOf(Effect.BONUS))
            {
                (it.countHandCards(Suit.LAND, Suit.WEATHER, Suit.FLOOD, Suit.FLAME)) * 5
            }
        ),
        collector to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                mapOf(
                    3 to 10,
                    4 to 40,
                    5 to 100,
                    6 to 100,
                    7 to 100,
                    8 to 100
                )[it.largestSuit()] ?: 0
            }
        ),
        beastmaster to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countHandCards(Suit.BEAST) * 9 },
            RuleAboutRule(listOf(Effect.CLEAR)) {
                it.identifyClearedPenalty { card ->
                    card.isOneOf(
                        Suit.BEAST
                    )
                }
            }
        ),
        warlockLord to listOf(
            RuleAboutScore(listOf(Effect.PENALTY)) {
                (it.countHandCards(Suit.LEADER) +
                        max(it.countHandCards(Suit.WIZARD) - 1, 0)) * -10
            }
        ),
        genie to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { (Player.all.size * 10) }
        ),
        judge to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { it.countCardWithAtLeastOnePenaltyNotCleared() * 10 }
        ),
        angel to listOf(unblankable),
        demon to listOf(
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK), 500) { game ->
                game.groupNotBlankedCardsBySuit()
                    .filter { it.key != Suit.OUTSIDER }
                    .filter { it.value.size == 1 }.map { it.value }
                    .flatten()
            }
        ),
        darkQueen to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (DiscardArea.instance.game().countHandCards(
                    Suit.LAND,
                    Suit.FLOOD,
                    Suit.FLAME,
                    Suit.WEATHER
                ) + DiscardArea.instance.game().containsHandCards(unicorn).toInt()) * 5
            }
        ),
        ghoul to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (DiscardArea.instance.game().countHandCards(
                    Suit.WIZARD, Suit.LEADER,
                    Suit.ARMY, Suit.BEAST, Suit.UNDEAD
                )) * 4
            }
        ),
        specter to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (DiscardArea.instance.game().countHandCards(
                    Suit.ARTIFACT, Suit.OUTSIDER,
                    Suit.WIZARD
                )) * 6
            }
        ),
        lich to listOf(
            unblankable,
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.UNDEAD) - 1 + it.containsHandCards(necromancer)
                    .toInt()) * 10
            },
            RuleAboutCard(listOf(Effect.BONUS, Effect.UNBLANKABLE)) {
                it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.UNDEAD) }
            }
        ),
        deathKnight to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (DiscardArea.instance.game().countHandCards(Suit.WEAPON, Suit.ARMY)) * 7
            }
        ),
        dungeon to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.UNDEAD, Suit.BEAST, Suit.ARTIFACT) +
                        it.containsHandCards(necromancer).toInt() +
                        it.containsHandCards(warlockLord).toInt() +
                        it.containsHandCards(demon).toInt()) * 5 +
                        (it.atLeastOneHandCardOf(Suit.UNDEAD)).toInt() * 5 +
                        (it.atLeastOneHandCardOf(Suit.BEAST)).toInt() * 5 +
                        (it.atLeastOneHandCardOf(Suit.ARTIFACT)).toInt() * 5
            }
        ),
        castle to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.BUILDING) - 1) * 5 +
                        (it.atLeastOneHandCardOf(Suit.LEADER)).toInt() * 10 +
                        (it.atLeastOneHandCardOf(Suit.ARMY)).toInt() * 10 +
                        (it.atLeastOneHandCardOf(Suit.LAND)).toInt() * 10 +
                        (it.countHandCards(Suit.BUILDING) - 1 > 0).toInt() * 5

            }
        ),
        crypt to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                it.handCardsNotBlanked().filter { card -> card.isOneOf(Suit.UNDEAD) }
                    .sumOf { card -> card.value() }
            },
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK, Suit.LEADER))
            { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.LEADER) } }
        ),
        chapel to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.WIZARD, Suit.OUTSIDER, Suit.UNDEAD, Suit.LEADER) == 2 &&
                        (it.countHandCards(Suit.WIZARD) == 2 ||
                                it.countHandCards(Suit.OUTSIDER) == 2 ||
                                it.countHandCards(Suit.UNDEAD) == 2 ||
                                it.countHandCards(Suit.LEADER) == 2)).toInt() * 40
            }
        ),
        garden to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.countHandCards(Suit.LEADER, Suit.BEAST)) * 11
            },
            RuleAboutCard(listOf(Effect.PENALTY, Effect.BLANK)) {
                if (it.countHandCards(Suit.UNDEAD) > 0 || it.containsHandCards(necromancer) || it.containsHandCards(
                        demon
                    )
                )
                    it.filterNotBlankedHandCards { card -> card.hasSameNameThan(garden) } else emptyList()
            }
        ),
        bellTowerV2 to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                (it.atLeastOneHandCardOf(Suit.WIZARD) || it.atLeastOneHandCardOf(Suit.UNDEAD)).toInt() * 15
            }
        ),
        fountainOfLifeV2 to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                it.handCardsNotBlanked().filter { card ->
                    card.isOneOf(
                        Suit.BUILDING, Suit.WEAPON, Suit.FLOOD,
                        Suit.FLAME, Suit.LAND, Suit.WEATHER
                    )
                }.maxOfOrNull { card -> card.value() } ?: 0
            }
        ),
        greatFloodV2 to listOf(
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.ARMY
                )
            ) { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.ARMY) } },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.BUILDING
                )
            ) { it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.BUILDING) } },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.LAND
                )
            ) { it.filterNotBlankedHandCards { card -> (card.isOneOf(Suit.LAND) && card.definition != mountain) } },
            RuleAboutCard(
                listOf(
                    Effect.PENALTY,
                    Effect.BLANK,
                    Suit.FLAME
                )
            ) { it.filterNotBlankedHandCards { card -> (card.isOneOf(Suit.FLAME) && card.definition != lightning) } }
        ),
        rangersV2 to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                it.countHandCards(
                    Suit.LAND,
                    Suit.BUILDING
                ) * 10
            },
            RuleAboutRule(
                listOf(
                    Effect.CLEAR,
                    Suit.ARMY
                )
            ) { it.identifyArmyClearedPenalty { true } }),
        necromancerV2 to listOf(
            RuleAboutCard(listOf(Effect.BONUS, Effect.UNBLANKABLE)) {
                it.filterNotBlankedHandCards { card -> card.isOneOf(Suit.UNDEAD) }
            }
        ),
        worldTreeV2 to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.handCardsNotBlanked()
                        .groupBy { card -> card.suit() }.entries.any { entry -> entry.value.size > 1 }
                ) 0 else 70
            }),
        spyglass to listOf(
            RuleAboutScore(listOf(Effect.PENALTY)) {
                if (Player.all.size == 2) -9 else 0
            }),
        treasure_chest to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) {
                if (it.countTableCards(Suit.CURSED_ITEM) > 3) 25 else 0
            }),

        jester to listOf(
            RuleAboutScore(listOf(Effect.BONUS)) { if (it.isAllHandCardsOdd()) 50 else (it.countOddHandCards() - 1) * 3 }
        )
    )

}
