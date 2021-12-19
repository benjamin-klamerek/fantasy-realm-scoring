package com.klamerek.fantasyrealms.screen

import android.os.SystemClock.sleep
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.assertion.RecyclerViewSize
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@LargeTest
class PlayerSelectionActivityTest {

    private val positiveButtonId = 16908313

    lateinit var scenario: ActivityScenario<PlayerSelectionActivity>

    @BeforeEach
    fun before() {
        scenario = ActivityScenario.launch(PlayerSelectionActivity::class.java)
        Intents.init()
        scenario.onActivity {
            it.removeAllPlayers(AllPlayersDeletionEvent())
        }
    }

    @AfterEach
    fun after() {
        Intents.release()
        scenario.close()
    }

    private fun addPlayer(name: String) {
        onView(withId(R.id.addPlayerButton)).perform(click())
        sleep(500)
        onView(withId(R.id.playerNameEditText)).perform(replaceText(name))
        sleep(500)
        onView(withId(positiveButtonId)).perform(click())
        sleep(500)
    }

    @Test
    fun add_new_player_named_ME() {
        addPlayer("ME")

        onView(allOf(withId(R.id.playerNameField), withText("ME"))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.scoreLabel), withText("0"), hasSibling(withText("ME")))).check(matches(isDisplayed()))
    }

    @Test
    fun delete_player_swipe_left() {
        addPlayer("ME")

        onView(withId(R.id.playersView)).check(RecyclerViewSize(1))
        onView(withId(R.id.playersView)).perform(RecyclerViewActions.actionOnItemAtPosition<PlayerSelectionAdapter.PlayerHolder>(0, swipeLeft()));
        onView(withId(R.id.playersView)).check(RecyclerViewSize(0))
    }

    @Test
    fun delete_player_swipe_right() {
        addPlayer("ME")

        onView(withId(R.id.playersView)).check(RecyclerViewSize(1))
        onView(withId(R.id.playersView)).perform(RecyclerViewActions.actionOnItemAtPosition<PlayerSelectionAdapter.PlayerHolder>(0, swipeRight()));
        onView(withId(R.id.playersView)).check(RecyclerViewSize(0))
    }

    @Test
    fun delete_middle_player() {
        addPlayer("ME1")
        addPlayer("ME2")
        addPlayer("ME3")

        onView(withId(R.id.playersView)).check(RecyclerViewSize(3))
        onView(withId(R.id.playersView)).perform(RecyclerViewActions.actionOnItemAtPosition<PlayerSelectionAdapter.PlayerHolder>(1, swipeLeft()));
        onView(withId(R.id.playersView)).check(RecyclerViewSize(2))
    }

    @Test
    fun rename_player(){
        addPlayer("ME")
        onView(allOf(withId(R.id.playerNameField), withText("ME"))).perform(longClick())
        onView(withId(R.id.playerNameEditText)).perform(replaceText("NEW_NAME"))
        sleep(500)
        onView(withId(positiveButtonId)).perform(click())
        sleep(500)
        onView(allOf(withId(R.id.playerNameField), withText("NEW_NAME"))).check(matches(isDisplayed()))
    }

    @Test
    fun edit_player_open_hand_activity() {
        addPlayer("ME")

        onView(withId(R.id.playersView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PlayerSelectionAdapter.PlayerHolder>(0,
                object : ViewAction {
                    val click = click()
                    override fun getConstraints(): Matcher<View> = click.constraints

                    override fun getDescription(): String = "click on custom image view"

                    override fun perform(uiController: UiController?, view: View?) {
                        click.perform(uiController, view?.findViewById(R.id.editButton));
                    }

                })
        )

        intended(hasComponent(HandSelectionActivity::class.java.name))
    }

}
