package com.klamerek.fantasyrealms

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.klamerek.fantasyrealms.game.CardDefinitions
import com.klamerek.fantasyrealms.util.LocaleManager
import com.klamerek.fantasyrealms.util.LocaleManager.english
import org.junit.jupiter.api.Test
import java.util.*

/**
 * Not a test but a simple generator about chip UI element (one per card)
 *
 */
class ChipGenerator {


    @Test
    fun generateChipXMLDefinition() {
        LocaleManager.updateContextWithPreferredLanguage(
            InstrumentationRegistry.getInstrumentation().targetContext, english
        )

        CardDefinitions.getAll().sortedBy { it.id }.forEach { definition ->
            val nameCleaned = definition.name().lowercase(Locale.getDefault()).replace(" ", "_")
            val suitCleaned = definition.suit.name.lowercase(Locale.getDefault())
            Log.d(
                "Generator", "<com.google.android.material.chip.Chip\n" +
                        "                android:layout_width=\"wrap_content\"\n" +
                        "                android:layout_height=\"wrap_content\" android:id=\"@+id/chip" + nameCleaned +
                        "\" android:text=\"@string/" + nameCleaned + "\"\n" +
                        "                android:checkable=\"true\"\n" +
                        "                android:tag=\"" + definition.id +
                        "\" app:chipBackgroundColor=\"@color/chip_background_" + suitCleaned + "\" \n" +
                        "                android:textColor=\"@color/chip_revert_color_activated\" \n " +
                        "       app:chipStrokeColor=\"@color/color" + definition.suit.display() + "\"\n" +
                        "       app:chipStrokeWidth=\"2dp\"/>"
            )
        }
    }

}
