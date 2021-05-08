package com.klamerek.fantasyrealms.screen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.ensureThatGooglePlayServicesUpToDate
import org.hamcrest.core.AllOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * TODO have to handle the first time permission requested by the camera !!!
 *
 */
@LargeTest
class ScanActivityTest {

    lateinit var scenario: ActivityScenario<ScanActivity>

    @BeforeEach
    fun before() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        ensureThatGooglePlayServicesUpToDate(appContext)
        scenario = ActivityScenario.launch(ScanActivity::class.java)
        Intents.init()
    }

    @AfterEach
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun scan_button_close_activity() {
        onView(AllOf.allOf(withId(R.id.cameraCaptureButton))).perform(click())

        //Have to be sure that activity is finished, haven't found yet a better way to achieve this
        Thread.sleep(2000)

        val result = scenario.result
        assertEquals(result.resultCode, Constants.RESULT_OK)
        assertTrue(result.resultData.hasExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID))
    }

}