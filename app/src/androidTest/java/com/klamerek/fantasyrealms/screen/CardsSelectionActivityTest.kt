package com.klamerek.fantasyrealms.screen

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.celestialKnights
import com.klamerek.fantasyrealms.game.greatFlood
import com.klamerek.fantasyrealms.game.rangers
import com.klamerek.fantasyrealms.util.Constants
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@LargeTest
class CardsSelectionActivityTest {

    private lateinit var scenario: ActivityScenario<CardsSelectionActivity>

    @BeforeEach
    fun before() {
        Intents.init()
    }

    @AfterEach
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun default_mode_with_3_cards_selected() {
        val cardsSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CardsSelectionActivity::class.java
        )
        val cardsSelectionExchange = CardsSelectionExchange()
        cardsSelectionIntent.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        scenario = ActivityScenario.launch(cardsSelectionIntent)

        Espresso.onView(ViewMatchers.withId(R.id.chiprangers)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.chipknights)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.chipgreat_flood)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.addCardsButton)).perform(click())

        val receivedIntent = scenario.result.resultData
        Assertions.assertThat(receivedIntent.hasExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID))
        val cardsSelectionIntentOutput = receivedIntent.getSerializableExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID
        ) as? CardsSelectionExchange
        Assertions.assertThat(cardsSelectionIntentOutput?.cardsSelected).containsExactlyInAnyOrder(
            rangers.id, greatFlood.id, celestialKnights.id
        )

    }

}
