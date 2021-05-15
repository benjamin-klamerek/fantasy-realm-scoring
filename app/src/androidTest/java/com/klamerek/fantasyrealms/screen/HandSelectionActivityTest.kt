package com.klamerek.fantasyrealms.screen

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


@LargeTest
class HandSelectionActivityTest {

    lateinit var scenario: ActivityScenario<HandSelectionActivity>

    private fun testPlayerWithNoHand() {
        Player.all.clear()
        Player.all.add(Player("TEST", Game()))
        scenario = ActivityScenario.launch(HandSelectionActivity::class.java)
        Intents.init()
        scenario.onActivity {
            it.removeAllCards(AllCardsDeletionEvent())
        }
    }

    @AfterEach
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun player_name_and_score_on_label() {
        val game = Game();
        game.add(basilisk)

        Player.all.clear()
        Player.all.add(Player("MY_NAME", game))

        val handSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HandSelectionActivity::class.java
        )
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, 0)
        scenario = ActivityScenario.launch(handSelectionIntent)

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 35")))
    }

    @Test
    fun hand_size() {
        val game = Game();
        game.add(basilisk)
        game.add(lightning)
        game.add(earthElemental)

        Player.all.clear()
        Player.all.add(Player("MY_NAME", game))

        val handSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HandSelectionActivity::class.java
        )
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, 0)
        scenario = ActivityScenario.launch(handSelectionIntent)

        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))
    }

    @Test
    fun score_and_hand_are_updated_after_card_removal() {
        val game = Game();
        game.add(basilisk)
        game.add(lightning)
        game.add(earthElemental)

        Player.all.clear()
        Player.all.add(Player("MY_NAME", game))

        val handSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HandSelectionActivity::class.java
        )
        handSelectionIntent.putExtra(Constants.PLAYER_SESSION_ID, 0)
        scenario = ActivityScenario.launch(handSelectionIntent)

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 50")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("3/7")))

        onView(withId(R.id.handView)).perform(RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(1, swipeLeft()))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 39")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("2/7")))
    }

    @Test
    fun scan_button() {
        testPlayerWithNoHand()

        onView(withId(R.id.scanButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ScanActivity::class.java.name))
    }

    @Test
    fun add_card_button() {
        testPlayerWithNoHand()

        onView(withId(R.id.addCardsButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(CardsSelectionActivity::class.java.name))
    }

}
