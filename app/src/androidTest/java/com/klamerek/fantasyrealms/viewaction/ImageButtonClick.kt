package com.klamerek.fantasyrealms.viewaction

import android.view.View
import android.widget.ImageButton
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class ImageButtonClick(private val buttonId: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(ImageButton::class.java)
    }

    override fun getDescription(): String {
        return "$buttonId click action"
    }

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<ImageButton>(buttonId)?.performClick()
    }

}
