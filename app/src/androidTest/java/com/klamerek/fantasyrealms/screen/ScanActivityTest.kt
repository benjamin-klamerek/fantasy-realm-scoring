package com.klamerek.fantasyrealms.screen

import android.os.SystemClock.sleep
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.util.Constants
import org.hamcrest.core.AllOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
@LargeTest
class ScanActivityTest {

    lateinit var scenario: ActivityScenario<ScanActivity>

    @BeforeEach
    fun before() {
        GrantPermissionRule.grant(android.Manifest.permission.CAMERA);
        scenario = ActivityScenario.launch(ScanActivity::class.java)
        Intents.init()
    }

    @AfterEach
    fun after() {
        Intents.release()
        scenario.close()
    }

    /**
     * Too many problems with this test, not yet found a solution with this camera library
     */
    @Test
    fun scan_button_close_activity() {
        onView(AllOf.allOf(withId(R.id.cameraCaptureButton))).perform(click())

        //Ensure that activity is finished, haven't found yet a better way to achieve this
        sleep(5000)

        val result = scenario.result
        assertEquals(result.resultCode, Constants.RESULT_OK)
        assertTrue(result.resultData.hasExtra(Constants.CARD_SELECTION_DATA_EXCHANGE_SESSION_ID))
    }

}
