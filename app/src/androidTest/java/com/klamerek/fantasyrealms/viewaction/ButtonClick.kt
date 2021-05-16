package com.klamerek.fantasyrealms.viewaction

import android.view.View
import android.widget.Button
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ButtonClick(private val buttonId: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(Button::class.java)
    }

    override fun getDescription(): String {
        return "$buttonId click action"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<Button>(buttonId)?.performClick()
    }

}
