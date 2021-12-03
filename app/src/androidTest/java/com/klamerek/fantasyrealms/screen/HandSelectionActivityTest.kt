package com.klamerek.fantasyrealms.screen

import android.app.Instrumentation
import android.content.Intent
import android.os.SystemClock
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
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.game.*
import com.klamerek.fantasyrealms.matcher.ChipMatcher
import com.klamerek.fantasyrealms.matcher.RecycleViewMatcher
import com.klamerek.fantasyrealms.util.Constants
import com.klamerek.fantasyrealms.util.Preferences
import com.klamerek.fantasyrealms.viewaction.ButtonClick
import com.klamerek.fantasyrealms.viewaction.ImageButtonClick
import org.greenrobot.eventbus.EventBus
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
        val handSelectionIntent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            HandSelectionActivity::class.java)
        handSelectionIntent.putExtra(Constants.GAME_SESSION_ID, 0)
        scenario = ActivityScenario.launch(handSelectionIntent)
        return scenario
    }

    @BeforeEach
    fun before() {
        Preferences.saveBuildingsOutsidersUndeadInPreferences(
            InstrumentationRegistry.getInstrumentation().targetContext, false)
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
        handSelectionIntent.putExtra(Constants.GAME_SESSION_ID, 1)
        scenario = ActivityScenario.launch(handSelectionIntent)

        onView(withId(R.id.playerNameLabel)).check(matches(withText("PLAYER 2 - Score : 35")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("1/7")))
    }

    @Test
    fun book_of_changes_updates_basilik_as_army() {
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
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, ButtonClick(R.id.effectButton))
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
    fun shapeshifter_updated_as_basilik() {
        initPlayer("MY_NAME", listOf(bookOfChanges, island, shapeshifter, mirage, doppelganger))

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        cardsSelectionExchange.cardsSelected.addAll(listOf(basilisk.id))
        cardsSelectionExchange.cardInitiator = shapeshifter.id

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(2, ButtonClick(R.id.effectButton))
        )

        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.cardNameLabel, 2,
                    ChipMatcher.backgroundColorResource(R.color.colorBeast)
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
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, ButtonClick(R.id.effectButton))
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
    fun mirage_updated_as_forest() {
        initPlayer("MY_NAME", listOf(bookOfChanges, island, shapeshifter, mirage, doppelganger))

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        cardsSelectionExchange.cardsSelected.addAll(listOf(forest.id))
        cardsSelectionExchange.cardInitiator = mirage.id

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(3, ButtonClick(R.id.effectButton))
        )

        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.cardNameLabel, 3,
                    ChipMatcher.backgroundColorResource(R.color.colorLand)
                )
            )
        )
    }

    @Test
    fun doppelganger_updated_as_basilik() {
        initPlayer("MY_NAME", listOf(bookOfChanges, island, shapeshifter, mirage, doppelganger, basilisk))

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        cardsSelectionExchange.cardsSelected.addAll(listOf(basilisk.id))
        cardsSelectionExchange.cardInitiator = doppelganger.id

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(4, ButtonClick(R.id.effectButton))
        )

        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(
                    R.id.cardNameLabel, 4,
                    ChipMatcher.backgroundColorResource(R.color.colorBeast)
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

    @Test
    fun delete_all_cards() {
        initPlayer("MY_NAME", listOf(basilisk, lightning, earthElemental))

        EventBus.getDefault().post(AllCardsDeletionEvent())

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 0")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("0/7")))
    }

    @Test
    fun display_card_details() {
        initPlayer("TEST", listOf(earthElemental, forest, bellTower, mountain))

        onView(withId(R.id.playerNameLabel)).check(matches(withText("TEST - Score : 73")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("4/7")))

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, ImageButtonClick(R.id.cardDetailButton))
        )

        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(R.id.baseValueLabel, 0, withText("4"))
            )
        )
        onView(withId(R.id.handView)).check(
            matches(
                RecycleViewMatcher.childOfViewAtPosition(R.id.bonusValueLabel, 0, withText("+45"))
            )
        )

    }

    /**
     * Scenario to fix bug : transformation effect are kept after clearing hand
     */
    @Test
    fun transformation_effects_are_cleared_after_clear_hand() {
        initPlayer("MY_NAME", listOf(greatFlood, beastmaster, princess, unicorn, basilisk, swordOfKeth, shieldOfKeth, bookOfChanges))

        //STEP1 : init hand

        val cardsSelectionExchange = CardsSelectionExchange()
        val cardSelection = Intent()
        cardSelection.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange)
        val mockedActivityResult = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection)
        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult)

        //STEP2 : set basilik as weather

        cardsSelectionExchange.cardsSelected.addAll(listOf(basilisk.id))
        cardsSelectionExchange.cardInitiator = bookOfChanges.id
        cardsSelectionExchange.suitsSelected = mutableListOf(Suit.WEATHER.name)

        onView(withId(R.id.handView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HandSelectionAdapter.HandHolder>(0, ButtonClick(R.id.effectButton))
        )

        //STEP3 : clear all hand

        EventBus.getDefault().post(AllCardsDeletionEvent())

        //STEP4 : re add cards expect book of change

        val cardsSelectionExchange2 = CardsSelectionExchange()
        cardsSelectionExchange2.cardsSelected.addAll(listOf(greatFlood.id, beastmaster.id, princess.id,
            unicorn.id, basilisk.id, swordOfKeth.id, shieldOfKeth.id))
        val cardSelection2 = Intent()
        cardSelection2.putExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID, cardsSelectionExchange2)
        val mockedActivityResult2 = Instrumentation.ActivityResult(Constants.RESULT_OK, cardSelection2)

        intending(hasComponent(CardsSelectionActivity::class.java.name)).respondWith(mockedActivityResult2)
        onView(withId(R.id.addCardsButton)).perform(click())

        //STEP5 : ensure that score meet expectation (basilik is no more a weather card)

        onView(withId(R.id.playerNameLabel)).check(matches(withText("MY_NAME - Score : 234")))
        onView(withId(R.id.handSizeLabel)).check(matches(withText("7/7")))

    }

}
