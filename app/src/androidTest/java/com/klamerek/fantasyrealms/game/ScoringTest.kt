package com.klamerek.fantasyrealms.game

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class ScoringTest {

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

        game.doppelgangerSelection = warlockLord
        game.shapeShifterSelection = king

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

        game.bookOfChangeSelection = Pair(bellTower, Suit.LEADER)

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
        Assertions.assertEquals(225, game.score())
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

        game.shapeShifterSelection = king
        game.mirageSelection = rainstorm

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

}
