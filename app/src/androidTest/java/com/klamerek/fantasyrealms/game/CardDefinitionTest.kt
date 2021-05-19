package com.klamerek.fantasyrealms.game

import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.util.LocaleManager
import com.klamerek.fantasyrealms.util.LocaleManager.english
import com.klamerek.fantasyrealms.util.LocaleManager.french
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CardDefinitionTest {

    @DisplayName("Effect display is dynamic from language parameter")
    @Test
    fun effect_display_is_dynamic_from_language_parameter() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, english
        )
        val englishWord = Effect.PENALTY.display()

        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, french
        )
        val frenchWord = Effect.PENALTY.display()

        Assertions.assertNotEquals(englishWord, frenchWord)
    }

    @DisplayName("Suit display is dynamic from language parameter")
    @Test
    fun suit_display_is_dynamic_from_language_parameter() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, english
        )
        val englishWord = Suit.FLOOD.display()

        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, french
        )
        val frenchWord = Suit.FLOOD.display()

        Assertions.assertNotEquals(englishWord, frenchWord)
    }

    @DisplayName("Card name is dynamic from language parameter")
    @Test
    fun card_name_is_dynamic_from_language_parameter() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, english
        )
        val englishWord = beastmaster.name()

        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, french
        )
        val frenchWord = beastmaster.name()

        Assertions.assertNotEquals(englishWord, frenchWord)
    }

    @DisplayName("Card rule is dynamic from language parameter")
    @Test
    fun card_rule_is_dynamic_from_language_parameter() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, english
        )
        val englishWord = beastmaster.rule()

        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, french
        )
        val frenchWord = beastmaster.rule()

        Assertions.assertNotEquals(englishWord, frenchWord)
    }

}
