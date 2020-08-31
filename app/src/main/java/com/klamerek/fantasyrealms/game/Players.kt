package com.klamerek.fantasyrealms.game

/**
 * A player with a name and a list of cards (from a game)
 *
 * @property name
 * @property game
 */
class Player(var name: String, val game: Game)

class Players {

    companion object {
       val instance : MutableList<Player> = ArrayList();
    }

}