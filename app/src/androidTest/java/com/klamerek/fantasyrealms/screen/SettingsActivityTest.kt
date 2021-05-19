package com.klamerek.fantasyrealms.screen

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

        val initialLanguage = LocaleManager.getLanguage(InstrumentationRegistry.getInstrumentation().targetContext)

        val newSelection = if (LocaleManager.english == initialLanguage) LocaleManager.french else LocaleManager.english
        onView(withId(R.id.languageSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(Language::class.java)), withMyValue(newSelection.label()))).perform(click())

        onView(withId(R.id.doneButton)).perform(click())

        val resultLanguage = LocaleManager.getLanguage(InstrumentationRegistry.getInstrumentation().targetContext)

        Assertions.assertThat(initialLanguage).isNotEqualTo(resultLanguage)
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
