package com.klamerek.fantasyrealms.screen

import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
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
    fun score_and_hand_are_updated_after_card_removal() {
        initPlayer("MY_NAME", listOf(basilisk, lightning, earthElemental))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 50")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))

        onView(withId(R.id.handView)).perform(RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(1, swipeLeft()))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 39")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("2/7")))
    }

    @Test
    fun add_3_cards_selection_to_hand() {
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
