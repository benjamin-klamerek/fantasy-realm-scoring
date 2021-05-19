package com.klamerek.fantasyrealms.screen

import android.text.style.StyleSpan
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.util.LocaleManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class VisualEffectsTest {

    @Test
    fun four_elements_replaced() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, LocaleManager.english
        )

        val result = colorSuitsAndBoldCardNames(
            InstrumentationRegistry.getInstrumentation().targetContext,
            "This is a simple text with Land, Flood, Wild and Magic Wand"
        )
        Assertions.assertThat(result.getSpans(0, result.length, StyleSpan::class.java)).hasSize(4)
    }

}
