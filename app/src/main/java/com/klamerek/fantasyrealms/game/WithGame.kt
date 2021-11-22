package com.klamerek.fantasyrealms.game

/**
 * Interface for all objects that contains a Game (Player, discard area, ...)
 */
interface WithGame {
    fun name(): String
    fun game(): Game
    fun displayScore(): Boolean = true
}
