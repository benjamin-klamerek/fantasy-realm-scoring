package com.klamerek.fantasyrealms.ocr

import android.graphics.Bitmap
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.klamerek.fantasyrealms.game.*
import com.klamerek.fantasyrealms.getBitmapFromTestAssets
import com.klamerek.fantasyrealms.isGooglePlayServicesUpToDate
import com.klamerek.fantasyrealms.setLocale
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@MediumTest
class CardTitleRecognizerTest {

    @BeforeEach
    fun beforeEach() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        if (!isGooglePlayServicesUpToDate(appContext)) {
            throw UnsupportedOperationException("Device requires newer version of Google service (ML kit)")
        }
    }

    @DisplayName("Empty image")
    @Test
    fun empty_image() {
        val bean = CardTitleRecognizer()
        val conf = Bitmap.Config.ARGB_8888
        val bitmap = Bitmap.createBitmap(400, 400, conf)
        val task = bean.process(InputImage.fromBitmap(bitmap, 0))

        Tasks.await(task)

        assertThat(task.result, Matchers.empty())
    }

    @DisplayName("Hand example, each card separated (english)")
    @Test
    fun hand_example_each_card_separated() {
        setLocale("en", "EN")
        val bean = CardTitleRecognizer()
        val task = bean.process(InputImage.fromBitmap(getBitmapFromTestAssets("cardsExample1.jpg"), 0))

        Tasks.await(task)

        assertThat(
            task.result, Matchers.containsInAnyOrder(
                beastmaster.id, forge.id, unicorn.id,
                princess.id, bellTower.id, bookOfChanges.id, candle.id
            )
        )
    }

    @DisplayName("Hand example 2, each card separated (english)")
    @Test
    fun hand_example_2_each_card_separated() {
        setLocale("en", "EN")
        val bean = CardTitleRecognizer()
        val task = bean.process(InputImage.fromBitmap(getBitmapFromTestAssets("cardsExample2.jpg"), 0))

        Tasks.await(task)

        assertThat(
            task.result, Matchers.containsInAnyOrder(
                rainstorm.id, waterElemental.id, whirlwind.id,
                basilisk.id, worldTree.id, airElemental.id, wildfire.id, swamp.id, bellTower.id, candle.id,
                greatFlood.id, lightning.id //Great flood and lighting are false positives but hard to detect
            )
        )
    }

    @DisplayName("Hand example 3, each card separated (english)")
    @Test
    fun hand_example_3_each_card_separated() {
        setLocale("en", "EN")
        val bean = CardTitleRecognizer()
        val task = bean.process(InputImage.fromBitmap(getBitmapFromTestAssets("cardsExample3.jpg"), 0))

        Tasks.await(task)

        assertThat(
            task.result, Matchers.containsInAnyOrder(
                shieldOfKeth.id, swordOfKeth.id, empress.id,
                celestialKnights.id, mirage.id, fireElemental.id, forge.id
            )
        )
    }

    @DisplayName("Title only (french)")
    @Test
    fun title_only_french() {
        setLocale("fr", "FR")
        val bean = CardTitleRecognizer()
        val task = bean.process(InputImage.fromBitmap(getBitmapFromTestAssets("cardExampleFrench.png"), 0))

        Tasks.await(task)

        assertThat(
            task.result, Matchers.containsInAnyOrder(
                mirage.id, doppelganger.id, unicorn.id, worldTree.id,
                king.id, swamp.id
            )
        )
    }

}