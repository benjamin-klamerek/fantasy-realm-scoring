package com.klamerek.fantasyrealms

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.game.allDefinitions
import org.junit.jupiter.api.Test

/**
 * Not a test but a simple generator about chip UI element (one per card)
 *
 */
class ChipGenerator {


    @Test
    fun generateChipXMLDefinition() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, "en", "EN"
        )

        allDefinitions.forEach { definition ->
            val nameCleaned = definition.name().toLowerCase().replace(" ", "_")
            val suitCleaned = definition.suit.name.toLowerCase()
            Log.d(
                "Generator", "<com.google.android.material.chip.Chip\n" +
                        "                android:layout_width=\"wrap_content\"\n" +
                        "                android:layout_height=\"wrap_content\" android:id=\"@+id/chip" + nameCleaned +
                        "\" android:text=\"@string/" + nameCleaned + "\"\n" +
                        "                android:checkable=\"true\"\n" +
                        "                android:tag=\"" + definition.id +
                        "\" app:chipBackgroundColor=\"@color/chip_background_" + suitCleaned + "\" \n" +
                        "                android:textColor=\"@color/chip_revert_color_activated\" \n " +
                        "       app:chipStrokeColor=\"@color/color" + definition.suit.display() +"\"\n" +
                        "       app:chipStrokeWidth=\"1dp\"/>"
            )
        }
    }

}
