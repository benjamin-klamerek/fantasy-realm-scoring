package com.klamerek.fantasyrealms.ocr

import android.os.Handler
import android.os.Looper
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.klamerek.fantasyrealms.game.CardDefinition
import com.klamerek.fantasyrealms.game.allDefinitions
import com.klamerek.fantasyrealms.game.empty
import me.xdrop.fuzzywuzzy.FuzzySearch
import normalize
import java.util.*
import kotlin.math.abs


/**
 * Recognize title card from images and provide list of card (ids) identified
 *
 */
class CardTitleRecognizer {

    private val recognizer = TextRecognition.getClient()
    private val cardByCleanedName = allDefinitions.map { cleanText(it.name()) to it }.toMap()

    /**
     * Put to lower case, remove everything which is not a letter and replace all accented characters per their base letter version<br>
     * (é -> e, à -> a, ...)
     *
     * @param input     text to clean
     * @return "Cleaned" text
     */
    private fun cleanText(input: String): String = input.toLowerCase(Locale.getDefault()).normalize().filter { it.isLetter() }

    private fun getMatchingResult(input: String): MatchingResult {
        val extractedResult = FuzzySearch.extractOne(input, cardByCleanedName.keys)
        return MatchingResult(input, cardByCleanedName[extractedResult.string] ?: empty, extractedResult.score, extractedResult.string)
    }

    /**
     * Matching result rule
     *
     * @property input              entry text
     * @property bestCardResult     best card based on score
     * @property score              score obtained
     * @property matchingKey        best matching key with input
     */
    class MatchingResult(private val input: String, val bestCardResult: CardDefinition, val score: Int, private val matchingKey: String) {

        /**
         * A match is considered valid when :
         * <ul>
         *     <li> matching text length difference is inferior to 4 </li>
         *     <li> score is greater than 75 </li>
         *     <li> best card result is not "empty" </li>
         * <ul>
         *
         */
        fun isAcceptable() = abs(input.length - matchingKey.length) < 4 && score > 75 && bestCardResult != empty

    }

    /**
     * Analyze input image and provide result as task
     *
     * @param inputImage        image containing card titles
     * @return                  task of card list identified (as ids)
     */
    fun process(inputImage: InputImage): Task<List<Int>> =
        recognizer.process(inputImage).continueWithTask { recognizeTextTask ->
            val taskCompletionSource = TaskCompletionSource<List<Int>>()
            Handler(Looper.getMainLooper()).post {
                val text = recognizeTextTask.result
                taskCompletionSource.setResult(text?.textBlocks?.asSequence()
                    ?.map { textBlock -> cleanText(textBlock.text) }
                    ?.filter { it.length > 2 }
                    ?.map { getMatchingResult(it) }
                    ?.filter { matching -> matching.isAcceptable() }
                    ?.map { matching -> matching.bestCardResult }
                    ?.toSet()
                    ?.map { it.id }
                    ?: emptyList())
            }
            taskCompletionSource.task
        }

}
