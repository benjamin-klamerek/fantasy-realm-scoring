package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings

class DiscardArea(private val displayId: Int, private val game: Game) : WithGame{

    override fun name(): String = Strings.get(displayId)
    override fun game(): Game = game
    override fun displayScore(): Boolean = false

    companion object {
        val instance: DiscardArea = DiscardArea(R.string.discard_area, Game(true))
    }
}
