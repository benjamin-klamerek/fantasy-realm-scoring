package com.klamerek.fantasyrealms.matcher

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.chip.Chip
import org.hamcrest.Description
import org.hamcrest.Matcher

object ChipMatcher {

    fun backgroundColorResource(colorResId: Int): Matcher<View> {
        return object : BoundedMatcher<View, Chip>(Chip::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("Checks that chip background color is $colorResId")
            }

            override fun matchesSafely(item: Chip?): Boolean =
                item?.chipBackgroundColor?.equals(item.context.getColorStateList(colorResId)) ?: false
        }
    }

}
