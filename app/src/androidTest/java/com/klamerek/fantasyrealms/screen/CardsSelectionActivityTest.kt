package com.klamerek.fantasyrealms.screen

import android.content.Intent
import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.Suit
import com.klamerek.fantasyrealms.game.celestialKnights
import com.klamerek.fantasyrealms.game.greatFlood
import com.klamerek.fantasyrealms.game.rangers
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.LocaleManager
import com.klamerek.fantasyrealms.util.Preferences
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
        cardsSelectionIntent.putExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID,
            cardsSelectionExchange
        )
        scenario = ActivityScenario.launch(cardsSelectionIntent)

        Espresso.onView(ViewMatchers.withId(R.id.chiprangers)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.chipknights)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.chipgreat_flood)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.addCardsButton)).perform(click())

        //Ensure that activity is finished, haven't found yet a better way to achieve this
        SystemClock.sleep(2000)

        val receivedIntent = scenario.result.resultData
        Assertions.assertThat(receivedIntent.hasExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID))
        val cardsSelectionIntentOutput = receivedIntent.getSerializableExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID
        ) as? CardsSelectionExchange
        Assertions.assertThat(cardsSelectionIntentOutput?.cardsSelected).containsExactlyInAnyOrder(
            rangers.id, greatFlood.id, celestialKnights.id
        )

    }

    @Test
    fun card_and_suit_selection_scenario() {
        val cardsSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CardsSelectionActivity::class.java
        )
        val cardsSelectionExchange = CardsSelectionExchange()
        cardsSelectionExchange.selectionMode = Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
        cardsSelectionExchange.cardsScope.addAll(
            mutableListOf(
                rangers.id,
                celestialKnights.id,
                greatFlood.id
            )
        )
        cardsSelectionIntent.putExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID,
            cardsSelectionExchange
        )
        scenario = ActivityScenario.launch(cardsSelectionIntent)

        Espresso.onView(ViewMatchers.withId(R.id.chipWizard)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.chipknights)).perform(scrollTo(), click())
        Espresso.onView(ViewMatchers.withId(R.id.addCardsButton)).perform(click())

        //Ensure that activity is finished, haven't found yet a better way to achieve this
        SystemClock.sleep(2000)

        val receivedIntent = scenario.result.resultData
        Assertions.assertThat(receivedIntent.hasExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID))
        val cardsSelectionIntentOutput = receivedIntent.getSerializableExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID
        ) as? CardsSelectionExchange
        Assertions.assertThat(cardsSelectionIntentOutput?.cardsSelected)
            .containsExactlyInAnyOrder(celestialKnights.id)
        Assertions.assertThat(cardsSelectionIntentOutput?.suitsSelected)
            .containsExactlyInAnyOrder(Suit.WIZARD.name)
    }

    @Test
    fun card_names_contains_number() {
        LocaleManager.saveLanguageInPreferences( InstrumentationRegistry.getInstrumentation().targetContext, LocaleManager.english)
        val context = LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, LocaleManager.english
        )
        Preferences.saveDisplayCardNumberInPreferences(context, true)

        val cardsSelectionIntent = Intent(context, CardsSelectionActivity::class.java)
        val cardsSelectionExchange = CardsSelectionExchange()
        cardsSelectionExchange.selectionMode = Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
        cardsSelectionExchange.cardsScope.addAll(mutableListOf(rangers.id))
        cardsSelectionIntent.putExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID,
            cardsSelectionExchange
        )
        scenario = ActivityScenario.launch(cardsSelectionIntent)

        Espresso.onView(ViewMatchers.withId(R.id.chiprangers))
            .check(ViewAssertions.matches(ViewMatchers.withText("Rangers (25/53)")))
    }

    @Test
    fun card_names_without_number() {
        LocaleManager.saveLanguageInPreferences( InstrumentationRegistry.getInstrumentation().targetContext, LocaleManager.english)
        val context = LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, LocaleManager.english
        )
        Preferences.saveDisplayCardNumberInPreferences(context, false)

        val cardsSelectionIntent = Intent(context, CardsSelectionActivity::class.java)
        val cardsSelectionExchange = CardsSelectionExchange()
        cardsSelectionExchange.selectionMode = Constants.CARD_LIST_SELECTION_MODE_ONE_CARD_AND_SUIT
        cardsSelectionExchange.cardsScope.addAll(mutableListOf(rangers.id))
        cardsSelectionIntent.putExtra(
            Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID,
            cardsSelectionExchange
        )
        scenario = ActivityScenario.launch(cardsSelectionIntent)

        Espresso.onView(ViewMatchers.withId(R.id.chiprangers))
            .check(ViewAssertions.matches(ViewMatchers.withText("Rangers")))
    }

}
