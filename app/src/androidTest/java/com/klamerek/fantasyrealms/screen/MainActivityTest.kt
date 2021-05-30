package com.klamerek.fantasyrealms.screen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.klamerek.fantasyrealms.R
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

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
    fun open_settings() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.settingsButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(SettingsActivity::class.java.name))
    }

    @Test
    fun start_game() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.startButton)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(PlayerSelectionActivity::class.java.name))
    }

}
