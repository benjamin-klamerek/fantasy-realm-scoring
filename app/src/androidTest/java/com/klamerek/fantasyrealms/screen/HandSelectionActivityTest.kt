package com.klamerek.fantasyrealms.screen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.Game
import com.klamerek.fantasyrealms.game.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@LargeTest
class HandSelectionActivityTest {

    lateinit var scenario: ActivityScenario<HandSelectionActivity>

    @BeforeEach
    fun before() {
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
    fun scan_button() {
        Espresso.onView(ViewMatchers.withId(R.id.scanButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(ScanActivity::class.java.name))
    }

    @Test
    fun add_card_button() {
        Espresso.onView(ViewMatchers.withId(R.id.addCardsButton)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(CardsSelectionActivity::class.java.name))
    }

}
