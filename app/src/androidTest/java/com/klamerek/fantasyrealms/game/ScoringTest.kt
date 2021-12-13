package com.klamerek.fantasyrealms.game

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class ScoringTest {

    @BeforeEach
    fun beforeEach() {
        DiscardArea.instance.game().clear()
    }

    @DisplayName("worst hand")
    @Test
    fun worst_hand() {
        val game = Game()
        game.add(necromancer)
        game.add(warlockLord)
        game.add(king)
        game.add(queen)
        game.add(warlord)
        game.add(empress)
        game.add(shapeshifter)
        game.add(doppelganger)

        game.applySelection(doppelganger, warlockLord)
        game.applySelection(shapeshifter, king)

        game.calculate()
        Assertions.assertEquals(-74, game.score())
    }

    @DisplayName("best hand")
    @Test
    fun best_hand() {
        val game = Game()
        game.add(bellTower)
        game.add(candle)
        game.add(necromancer)
        game.add(warhorse)
        game.add(swordOfKeth)
        game.add(shieldOfKeth)
        game.add(gemOfOrder)
        game.add(bookOfChanges)

        game.applySelection(bookOfChanges, bellTower, Suit.LEADER)

        game.calculate()
        Assertions.assertEquals(397, game.score())
    }

    @DisplayName("gem of order is effective (100 points)")
    @Test
    fun gem_of_order_is_effective() {
        val game = Game()
        game.add(gemOfOrder)
        game.add(airElemental)
        game.add(necromancer)
        game.add(rainstorm)
        game.add(warhorse)
        game.add(dragon)
        game.add(bellTower)
        game.add(forest)

        game.calculate()
        Assertions.assertEquals(239, game.score())
    }

    @DisplayName("Unicorn and princess")
    @Test
    fun unicorn_with_princess() {
        val game = Game()
        game.add(unicorn)
        game.add(princess)
        game.add(queen)

        game.calculate()
        Assertions.assertEquals(9 + 2 + 6 + 8 + 30, game.score())
    }

    @DisplayName("WarlockLord rule")
    @Test
    fun warlockLord_rule() {
        val game = Game()
        game.add(warlockLord)
        game.add(beastmaster)
        game.add(elementalEnchantress)

        game.calculate()
        Assertions.assertEquals(5 + 9 + 5, game.score())
    }

    @DisplayName("mirage combo with shapeshifter")
    @Test
    fun mirage_combo_with_shapeshifter() {
        val game = Game()
        game.add(shieldOfKeth)
        game.add(basilisk)
        game.add(protectionRune)
        game.add(mirage)
        game.add(whirlwind)
        game.add(blizzard)
        game.add(shapeshifter)

        game.applySelection(shapeshifter, king)
        game.applySelection(mirage, rainstorm)

        game.calculate()
        Assertions.assertEquals(138, game.score())
    }

    @DisplayName("full game sample 1")
    @Test
    fun full_game_sample_1() {
        val game = Game()
        game.add(collector)
        game.add(unicorn)
        game.add(bellTower)
        game.add(magicWand)
        game.add(smoke)
        game.add(warlord)
        game.add(warlockLord)

        game.calculate()
        Assertions.assertEquals(74, game.score())
    }

    @DisplayName("full game sample 2")
    @Test
    fun full_game_sample_2() {
        val game = Game()
        game.add(forest)
        game.add(elvenArchers)
        game.add(elementalEnchantress)
        game.add(king)
        game.add(fireElemental)
        game.add(worldTree)
        game.add(blizzard)
        game.calculate()
        Assertions.assertEquals(133, game.score())
    }

    @DisplayName("full game sample 3")
    @Test
    fun full_game_sample_3() {
        val game = Game()
        game.add(forge)
        game.add(blizzard)
        game.add(unicorn)
        game.add(rainstorm)
        game.add(warDirigible)
        game.add(warlord)
        game.add(swordOfKeth)
        game.calculate()
        Assertions.assertEquals(58, game.score())
    }

    @DisplayName("full game sample 4")
    @Test
    fun full_game_sample_4() {
        val game = Game()
        game.add(dragon)
        game.add(fireElemental)
        game.add(blizzard)
        game.add(candle)
        game.add(queen)
        game.add(elvenArchers)
        game.add(worldTree)
        game.calculate()
        Assertions.assertEquals(39, game.score())
    }

    @DisplayName("full game sample 5")
    @Test
    fun full_game_sample_5() {
        val game = Game()
        game.add(warlockLord)
        game.add(warhorse)
        game.add(fountainOfLife)
        game.add(beastmaster)
        game.add(gemOfOrder)
        game.add(rangers)
        game.add(empress)
        game.calculate()
        Assertions.assertEquals(80, game.score())
    }

    @DisplayName("full game sample 6")
    @Test
    fun full_game_sample_6() {
        val game = Game()
        game.add(candle)
        game.add(protectionRune)
        game.add(elvenLongbow)
        game.add(forge)
        game.add(warship)
        game.add(earthElemental)
        game.add(blizzard)
        game.calculate()
        Assertions.assertEquals(99, game.score())
    }

    @DisplayName("full game sample 7")
    @Test
    fun full_game_sample_7() {
        val game = Game()
        game.add(mountain)
        game.add(undergroundCaverns)
        game.add(wildfire)
        game.add(rangers)
        game.add(beastmaster)
        game.add(basilisk)
        game.add(warship)
        game.calculate()
        Assertions.assertEquals(58, game.score())
    }

    @DisplayName("full game sample 8")
    @Test
    fun full_game_sample_8() {
        val game = Game()
        game.add(dragon)
        game.add(unicorn)
        game.add(basilisk)
        game.add(smoke)
        game.add(swamp)
        game.add(hydra)
        game.add(forest)
        game.calculate()
        Assertions.assertEquals(72, game.score())
    }

    @DisplayName("full game sample 9")
    @Test
    fun full_game_sample_9() {
        val game = Game()
        game.add(elementalEnchantress)
        game.add(undergroundCaverns)
        game.add(bellTower)
        game.add(worldTree)
        game.add(waterElemental)
        game.add(fireElemental)
        game.add(forest)
        game.calculate()
        Assertions.assertEquals(76, game.score())
    }

    @DisplayName("full game sample 10")
    @Test
    fun full_game_sample_10() {
        val game = Game()
        game.add(mountain)
        game.add(greatFlood)
        game.add(rainstorm)
        game.add(smoke)
        game.add(whirlwind)
        game.add(wildfire)
        game.add(elementalEnchantress)
        game.calculate()
        Assertions.assertEquals(137, game.score())
    }

    @DisplayName("fountain of life take only one card (the best one)")
    @Test
    fun fountain_of_life_take_only_one_card() {
        val game = Game()
        game.add(fountainOfLife)
        game.add(swamp)
        game.add(greatFlood)
        game.add(island)
        game.add(waterElemental)
        game.calculate()
        Assertions.assertEquals(161, game.score())
    }

    @DisplayName("Dwarvish infantry applies on OTHER armies")
    @Test
    fun dwarvish_infantry_applies_on_OTHER_armies() {
        val game = Game()
        game.add(dwarvishInfantry)
        game.add(worldTree)
        game.add(unicorn)
        game.add(greatFlood)
        game.add(forest)
        game.add(empress)
        game.add(warship)
        game.calculate()
        Assertions.assertEquals(171, game.score())
    }

    @DisplayName("Jester with all cards as odd")
    @Test
    fun jester_with_all_cards_as_odd() {
        val game = Game()
        game.add(forge)
        game.add(unicorn)
        game.add(jester)
        game.add(warship)
        game.calculate()
        Assertions.assertEquals(71, game.score())
    }

    @DisplayName("Jester with one card not odd")
    @Test
    fun jester_with_one_card_not_odd() {
        val game = Game()
        game.add(forge)
        game.add(unicorn)
        game.add(jester)
        game.add(hydra)
        game.add(warship)
        game.calculate()
        Assertions.assertEquals(39, game.score())
    }

    @DisplayName("Elven archers with weather")
    @Test
    fun elven_archers_with_weather() {
        val game = Game()
        game.add(elvenArchers)
        game.add(rainstorm)
        game.calculate()
        Assertions.assertEquals(18, game.score())
    }

    @DisplayName("Genie with 4 players")
    @Test
    fun genie_with_4_players() {
        Player.all.add(Player("1", Game()))
        Player.all.add(Player("2", Game()))
        Player.all.add(Player("3", Game()))
        Player.all.add(Player("4", Game()))
        val game = Game()
        game.add(genie)
        game.calculate()
        Assertions.assertEquals(-10, game.score())
    }

    @DisplayName("Judge with and without penalty")
    @Test
    fun judge_with_and_without_penalty() {
        val game = Game()
        game.add(judge)
        game.add(celestialKnights)
        game.add(lightCavalry)
        game.calculate()
        Assertions.assertEquals(60, game.score())

        game.add(protectionRune)
        game.calculate()
        Assertions.assertEquals(49, game.score())
    }

    @DisplayName("Angel is unblankable")
    @Test
    fun angel_is_unblankable() {
        val game = Game()
        game.add(angel)
        game.add(wildfire)
        game.calculate()
        Assertions.assertEquals(56, game.score())
    }

    @DisplayName("Angel selection is unblankable")
    @Test
    fun angel_selection() {
        val game = Game()
        game.add(angel)
        game.add(wildfire)
        game.add(celestialKnights)
        game.applySelection(angel, celestialKnights)
        game.calculate()
        Assertions.assertEquals(68, game.score())
    }

    @DisplayName("Angel selection is unblankable 2")
    @Test
    fun angel_selection_2() {
        val game = Game()
        game.add(angel)
        game.add(basilisk)
        game.add(queen)
        game.applySelection(angel, queen)
        game.calculate()
        Assertions.assertEquals(57, game.score())
    }

    @DisplayName("Demon with wildfire")
    @Test
    fun demon_with_wildfire() {
        val game = Game()
        game.add(wildfire)
        game.add(demon)
        game.calculate()
        Assertions.assertEquals(45, game.score())
    }

    @DisplayName("Demon applies after warship clearing penalties")
    @Test
    fun demon_applies_after_warship_clearing_penalties() {
        val game = Game()
        game.add(warship)
        game.add(demon)
        game.add(waterElemental)
        game.add(swamp)
        game.add(elvenArchers)
        game.add(lightCavalry)
        game.calculate()
        Assertions.assertEquals(114, game.score())
    }

    @DisplayName("Demon with wild cards")
    @Test
    fun demon_with_wild_cards() {
        val game = Game()
        game.add(demon)
        game.add(garden)
        game.add(earthElemental)
        game.add(shapeshifterV2)
        game.add(mirageV2)
        game.add(doppelganger)
        game.applySelection(doppelganger, garden)
        game.calculate()
        Assertions.assertEquals(49, game.score())
    }

    @DisplayName("Indirect blanking case (Great flood -> candle -> smoke)")
    @Test
    fun indirect_blanking_case_with_greatflood_candle_smoke() {
        val game = Game()
        game.add(smoke)
        game.add(candle)
        game.add(greatFlood)
        game.calculate()
        Assertions.assertEquals(32, game.score())
    }

    @DisplayName("Some specific cases with Warship and Book of changes")
    @Test
    fun some_specific_cases_with_Warship_and_book_of_changes() {
        val game = Game()
        game.add(demon)
        game.add(warship)
        game.add(greatFlood)
        game.add(waterElemental)
        game.add(elvenArchers)
        game.add(lightCavalry)
        game.calculate()
        Assertions.assertEquals(128, game.score())

        game.clear()
        game.add(bookOfChanges)
        game.add(warship)
        game.add(elvenArchers)
        game.add(dwarvishInfantry)
        game.applySelection(bookOfChanges, dwarvishInfantry, Suit.FLOOD)
        game.calculate()
        Assertions.assertEquals(56, game.score())

        game.clear()
        game.add(bookOfChanges)
        game.add(warship)
        game.add(warDirigible)
        game.applySelection(bookOfChanges, warDirigible, Suit.FLOOD)
        game.calculate()
        Assertions.assertEquals(61, game.score())

        game.clear()
        game.add(bookOfChanges)
        game.add(warship)
        game.add(warDirigible)
        game.add(lightCavalry)
        game.add(airElemental)
        game.applySelection(bookOfChanges, warDirigible, Suit.FLOOD)
        game.calculate()
        Assertions.assertEquals(47, game.score())
    }

    @DisplayName("Dark queen example")
    @Test
    fun dark_queen_example() {
        DiscardArea.instance.game().add(candle)
        DiscardArea.instance.game().add(undergroundCaverns)
        DiscardArea.instance.game().add(shieldOfKeth)

        val game = Game()
        game.add(darkQueen)
        game.calculate()

        Assertions.assertEquals(20, game.score())
    }

    @DisplayName("Ghoul example")
    @Test
    fun ghoul_example() {
        DiscardArea.instance.game().add(celestialKnights)
        DiscardArea.instance.game().add(undergroundCaverns)
        DiscardArea.instance.game().add(darkQueen)
        DiscardArea.instance.game().add(king)

        val game = Game()
        game.add(ghoul)
        game.calculate()

        Assertions.assertEquals(8 + 4 + 4 + 4, game.score())
    }

    @DisplayName("Specter example")
    @Test
    fun specter_example() {
        DiscardArea.instance.game().add(protectionRune)
        DiscardArea.instance.game().add(undergroundCaverns)
        DiscardArea.instance.game().add(darkQueen)
        DiscardArea.instance.game().add(demon)

        val game = Game()
        game.add(specter)
        game.calculate()

        Assertions.assertEquals(24, game.score())
    }

    @DisplayName("Lich example")
    @Test
    fun lich_example() {
        val game = Game()
        game.add(lich)
        game.add(necromancer)
        game.add(darkQueen)
        game.calculate()

        Assertions.assertEquals(46, game.score())
    }

    @DisplayName("Lich UNBLANKABLE")
    @Test
    fun lich_unblankable() {
        val game = Game()
        game.add(lich)
        game.add(wildfire)
        game.add(darkQueen)
        game.calculate()

        Assertions.assertEquals(73, game.score())
    }

    @DisplayName("Death Knight example")
    @Test
    fun death_knight_example() {
        DiscardArea.instance.game().add(celestialKnights)
        DiscardArea.instance.game().add(swordOfKeth)
        DiscardArea.instance.game().add(darkQueen)
        DiscardArea.instance.game().add(demon)

        val game = Game()
        game.add(deathKnight)
        game.calculate()

        Assertions.assertEquals(28, game.score())
    }

    @DisplayName("Dungeon example")
    @Test
    fun dungeon_example() {
        val game = Game()
        game.add(dungeon)
        game.add(darkQueen)
        game.add(unicorn)
        game.add(warlockLord)
        game.calculate()

        Assertions.assertEquals(76, game.score())
    }

    @DisplayName("Castle example")
    @Test
    fun castle_example() {
        val game = Game()
        game.add(castle)
        game.add(dungeon)
        game.add(king)
        game.add(queen)
        game.calculate()

        Assertions.assertEquals(51, game.score())
    }

    @DisplayName("Crypt example")
    @Test
    fun crypt_example() {
        val game = Game()
        game.add(crypt)
        game.add(darkQueen)
        game.add(ghoul)
        game.add(king)
        game.add(queen)
        game.calculate()

        Assertions.assertEquals(57, game.score())
    }

    @DisplayName("Chapel not activated because too many cards")
    @Test
    fun chapel_not_activated_because_too_many_cards() {
        val game = Game()
        game.add(chapel)
        game.add(king)
        game.add(queen)
        game.add(judge)
        game.calculate()

        Assertions.assertEquals(27, game.score())
    }

    @DisplayName("Chapel activated")
    @Test
    fun chapel_activated() {
        val game = Game()
        game.add(chapel)
        game.add(king)
        game.add(queen)
        game.calculate()

        Assertions.assertEquals(56, game.score())
    }

    @DisplayName("Garden example")
    @Test
    fun garden_activated() {
        val game = Game()
        game.add(garden)
        game.add(king)
        game.add(queen)
        game.add(unicorn)
        game.calculate()

        Assertions.assertEquals(82, game.score())
    }

    @DisplayName("Garden blanked")
    @Test
    fun garden_blanked() {
        val game = Game()
        game.add(garden)
        game.add(king)
        game.add(queen)
        game.add(unicorn)
        game.add(darkQueen)
        game.calculate()

        Assertions.assertEquals(48, game.score())
    }

    @DisplayName("Fountain of life v2 example")
    @Test
    fun fountain_of_life_v2_example() {
        val game = Game()
        game.add(fountainOfLifeV2)
        game.add(rainstorm)
        game.add(whirlwind)
        game.calculate()

        Assertions.assertEquals(45, game.score())
    }

    @DisplayName("Count OTHER cards with book of change")
    @Test
    fun count_OTHER_cards_with_book_of_change() {
        val game = Game()
        game.add(whirlwind)
        game.add(airElemental)
        game.add(bookOfChanges)
        game.applySelection(bookOfChanges, airElemental, Suit.BEAST)
        game.calculate()

        Assertions.assertEquals(35, game.score())
    }

    @DisplayName("Phoenix counts also as Flame and Weather (elemental case)")
    @Test
    fun phoenix_counts_also_as_flame_and_weather_elemental_case(){
        val game = Game()
        game.add(phoenix)
        game.add(airElemental)
        game.add(fireElemental)
        game.calculate()

        Assertions.assertEquals(4 + 4 + 30 + 14, game.score())
    }

    @DisplayName("Phoenix counts also as Flame and Weather (collector and blank effect)")
    @Test
    fun phoenix_counts_also_as_flame_and_weather_collector_and_blank(){
        val game = Game()
        game.add(phoenix)
        game.add(collector)
        game.add(rainstorm)
        game.add(blizzard)
        game.add(smoke)
        game.calculate()

        Assertions.assertEquals(0 + 7 + 8 + 30 + 27 + 40 -10, game.score())
    }

    @DisplayName("Phoenix is blanked with water elemental")
    @Test
    fun phoenix_is_blanked_with_water_elemental(){
        val game = Game()
        game.add(phoenix)
        game.add(waterElemental)
        game.calculate()

        Assertions.assertEquals(4, game.score())
    }

}
