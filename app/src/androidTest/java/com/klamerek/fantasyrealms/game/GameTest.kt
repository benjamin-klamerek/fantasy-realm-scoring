package com.klamerek.fantasyrealms.game

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class GameTest {

    @DisplayName("Same card cannot be added twice")
    @Test
    fun same_card_cannot_be_added_twice() {
        val game = Game()
        game.add(necromancer)
        game.add(necromancer)

        Assertions.assertThat(game.cards()).hasSize(1)
    }

    @DisplayName("Remove card")
    @Test
    fun remove_card() {
        val game = Game()
        game.add(necromancer)
        game.add(doppelganger)
        game.add(candle)

        game.remove(doppelganger)

        Assertions.assertThat(game.cards()).hasSize(2)
    }

    @DisplayName("Update card list")
    @Test
    fun update_card_list() {
        val game = Game()
        game.add(necromancer)
        game.add(candle)

        game.update(listOf(candle, collector, dragon))


        org.junit.jupiter.api.Assertions.assertAll(
            { Assertions.assertThat(game.cards()).hasSize(3) },
            {
                Assertions.assertThat(game.cards().map { it.definition })
                    .contains(candle, collector, dragon)
            }
        )
    }

}
