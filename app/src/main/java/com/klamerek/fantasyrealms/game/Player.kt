package com.klamerek.fantasyrealms.game

/**
 * A player with a name and a list of cards (from a game)
 *
 * @property name
 * @property game
 */
class Player(var name: String, val game: Game) {

    companion object {
        val all : MutableList<Player> = ArrayList()

        fun generateNextPlayerName(): String {
            var number = 1
            var playerNamePattern = "Player $number"
            while (all.firstOrNull { playerNamePattern == it.name } != null) {
                number++
                playerNamePattern = "Player $number"
            }
            return playerNamePattern
        }

    }
}
