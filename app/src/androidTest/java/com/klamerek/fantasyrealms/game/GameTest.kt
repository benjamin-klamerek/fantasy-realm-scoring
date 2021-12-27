package com.klamerek.fantasyrealms.game

import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.util.Preferences
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

        Assertions.assertThat(game.handCards()).hasSize(1)
    }

    @DisplayName("Remove card")
    @Test
    fun remove_card() {
        val game = Game()
        game.add(necromancer)
        game.add(doppelganger)
        game.add(candle)

        game.remove(doppelganger)

        Assertions.assertThat(game.handCards()).hasSize(2)
    }

    @DisplayName("Update card list")
    @Test
    fun update_card_list() {
        val game = Game()
        game.add(necromancer)
        game.add(candle)

        game.update(listOf(candle, collector, dragon))


        org.junit.jupiter.api.Assertions.assertAll(
            { Assertions.assertThat(game.handCards()).hasSize(4) },
            {
                Assertions.assertThat(game.handCards().map { it.definition })
                    .contains(necromancer, candle, collector, dragon)
            }
        )
    }

    @DisplayName("Max hand size")
    @Test
    fun max_hand_size(){
        Preferences.saveBuildingsOutsidersUndeadInPreferences(
            InstrumentationRegistry.getInstrumentation().targetContext, true)

        val game = Game()
        game.add(necromancer)
        game.add(necromancerV2)
        game.add(genie)
        game.add(leprechaun)

        Assertions.assertThat(
            game.handSizeExpected(InstrumentationRegistry.getInstrumentation().targetContext)).isEqualTo(11)
    }

}
