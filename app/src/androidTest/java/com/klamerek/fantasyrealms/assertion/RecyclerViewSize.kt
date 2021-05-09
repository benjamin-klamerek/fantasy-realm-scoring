package com.klamerek.fantasyrealms.assertion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

class RecyclerViewSize(private val numberOfElements: Int) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        ViewMatchers.assertThat((view as RecyclerView).adapter?.itemCount == numberOfElements, Matchers.`is`(true))
    }
}
