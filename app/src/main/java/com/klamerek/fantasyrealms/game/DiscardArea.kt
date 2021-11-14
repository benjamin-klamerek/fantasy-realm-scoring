package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings

class DiscardArea(val displayId: Int, val game: Game) {

    fun name(): String = Strings.get(displayId)

    companion object {
        val instance: DiscardArea = DiscardArea(R.string.discard_area, Game())
    }
}
