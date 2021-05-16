package com.klamerek.fantasyrealms.screen

import android.app.Instrumentation
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
import com.klamerek.fantasyrealms.matcher.ChipMatcher
import com.klamerek.fantasyrealms.matcher.RecycleViewMatcher
import org.hamcrest.Matcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@LargeTest
class HandSelectionActivityTest {

    private lateinit var scenario: ActivityScenario<HandSelectionActivity>

    private fun initPlayer(playerName: String, actualHand: List<CardDefinition>): ActivityScenario<HandSelectionActivity> {
        val game = Game()
        actualHand.forEach { game.add(it) }
        Player.all.clear()
        Player.all.add(Player(playerName, game))
        scenario = ActivityScenario.launch(HandSelectionActivity::class.java)
        return scenario
    }

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
    fun player_name_and_score_on_label() {
        initPlayer("MY_NAME", listOf(basilisk))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 35")))
    }

    @Test
    fun hand_size() {
        initPlayer("MY_NAME", listOf(basilisk, lightning, earthElemental))

        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))
    }

    @Test
    fun select_specific_player() {
        val game = Game()
        game.add(basilisk)
        Player.all.clear()
        Player.all.add(Player("PLAYER 1", Game()))
        Player.all.add(Player("PLAYER 2", game))
        Player.all.add(Player("PLAYER 3", Game()))

        val handSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HandSelectionActivity::class.java
        )
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, 1)
        scenario = ActivityScenario.launch(handSelectionIntent)

        onView(withId(R.id.playerNameLabel)).check(matches(withText("PLAYER 2 - Score : 35")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("1/7")))
    }

    class EffectButtonClick : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(Button::class.java)
        }

        override fun getDescription(): String {
            return "effectButton click action"
        }

        override fun perform(uiController: UiController?, view: View?) {
            view?.findViewById<Button>(R.id.effectButton)?.performClick()
        }

    }

    @Test
    fun book_of_changes_update_basilik_as_army() {
        initPlayer("MY_NAME", listOf(bookOfChanges, island, shapeshifter, mirage, doppelganger, basilisk))

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        cardsSelectionExchange.cardsSelected.addAll(listOf(basilisk.id))
        cardsSelectionExchange.cardInitiator = bookOfChanges.id
        cardsSelectionExchange.suitsSelected = mutableListOf(Suit.ARMY.name)

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, EffectButtonClick())
        )

        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.cardNameLabel, 5,
                    ChipMatcher.backgroundColorResource(R.color.colorArmy)
                )
            )
        )
    }

    @Test
    fun island_clear_penalty_on_wildfire() {
        initPlayer("MY_NAME", listOf(island, wildfire, rangers))

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        cardsSelectionExchange.cardsSelected.addAll(listOf(wildfire.id))
        cardsSelectionExchange.cardInitiator = island.id

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 54")))
        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.scoreLabel, 2,
                    withText("0")
                )
            )
        )

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, EffectButtonClick())
        )

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 59")))
        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.scoreLabel, 2,
                    withText("5")
                )
            )
        )
    }

    @Test
    fun score_and_hand_are_updated_after_card_removal() {
        initPlayer("MY_NAME", listOf(basilisk, lightning, earthElemental))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 50")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))

        onView(withId(R.id.handView)).perform(RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(1, swipeLeft()))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 39")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("2/7")))
    }

    @Test
    fun cards_selection_update_display_and_override_existing_hand() {
        val cardsSelectionExchange = CardsSelectionExchange()
        cardsSelectionExchange.cardsSelected.addAll(listOf(basilisk.id, forge.id, earthElemental.id))
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val result = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)

        initPlayer("TEST", listOf(fireElemental))

        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(result)
        onView(withId(R.id.addCardsButton)).perform(click())

        onView(withId(R.id.playerNameLabel)).check(matches(withText("TEST - Score : 48")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))
    }

    @Test
    fun scan_button() {
        initPlayer("TEST", emptyList())

        onView(withId(R.id.scanButton)).perform(click())
        Intents.intended(hasComponent(ScanActivity::class.java.name))
    }

    @Test
    fun add_card_button() {
        initPlayer("TEST", emptyList())

        onView(withId(R.id.addCardsButton)).perform(click())
        Intents.intended(hasComponent(CardsSelectionActivity::class.java.name))
    }

}
