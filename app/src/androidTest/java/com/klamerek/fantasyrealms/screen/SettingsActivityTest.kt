package com.klamerek.fantasyrealms.screen

import android.os.SystemClock.sleep
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.util.Language
import com.klamerek.fantasyrealms.util.LocaleManager
import com.klamerek.fantasyrealms.util.Preferences
import org.assertj.core.api.Assertions
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SettingsActivityTest {

    private lateinit var scenario: ActivityScenario<SettingsActivity>

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
    fun set_another_language() {
        scenario = ActivityScenario.launch(SettingsActivity::class.java)

        val initialLanguage =
            LocaleManager.getLanguage(InstrumentationRegistry.getInstrumentation().targetContext)

        val newSelection =
            if (LocaleManager.english == initialLanguage) LocaleManager.french else LocaleManager.english
        onView(withId(R.id.languageSpinner)).perform(click())
        onData(
            allOf(
                `is`(instanceOf(Language::class.java)),
                withMyValue(newSelection.label())
            )
        ).perform(click())

        onView(withId(R.id.doneButton)).perform(click())

        sleep(500)

        val resultLanguage =
            LocaleManager.getLanguage(InstrumentationRegistry.getInstrumentation().targetContext)

        Assertions.assertThat(initialLanguage).isNotEqualTo(resultLanguage)
    }

    @Test
    fun buildings_outsiders_undead_checkbox_linked_with_preference() {
        val results = ArrayList<Boolean>()
        repeat(2) {
            scenario = ActivityScenario.launch(SettingsActivity::class.java)
            val initialValue =
                Preferences.getBuildingsOutsidersUndead(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withId(R.id.withBuildingsOutsidersUndeadCheckBox)).perform(click())

            onView(withId(R.id.doneButton)).perform(click())

            sleep(500)

            val newValue =
                Preferences.getBuildingsOutsidersUndead(InstrumentationRegistry.getInstrumentation().targetContext)
            results.add(newValue)
            Assertions.assertThat(initialValue).isNotEqualTo(newValue)
        }
        Assertions.assertThat(results).hasSize(2)
        Assertions.assertThat(results[0]).isNotEqualTo(results[1])
    }

    @Test
    fun cursed_items_linked_with_preference() {
        val results = ArrayList<Boolean>()
        repeat(2) {
            scenario = ActivityScenario.launch(SettingsActivity::class.java)
            val initialValue =
                Preferences.getCursedItems(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withId(R.id.withCursedItemsCheckBox)).perform(click())

            onView(withId(R.id.doneButton)).perform(click())

            sleep(500)

            val newValue =
                Preferences.getCursedItems(InstrumentationRegistry.getInstrumentation().targetContext)
            results.add(newValue)
            Assertions.assertThat(initialValue).isNotEqualTo(newValue)
        }
        Assertions.assertThat(results).hasSize(2)
        Assertions.assertThat(results[0]).isNotEqualTo(results[1])
    }

    private fun <T> withMyValue(name: String): Matcher<T>? {
        return object : BaseMatcher<T>() {
            override fun matches(item: Any): Boolean {
                return item.toString() == name
            }

            override fun describeTo(description: Description?) {}
        }
    }

}
