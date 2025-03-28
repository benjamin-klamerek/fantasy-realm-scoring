package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R

// Looking at the other card definitions, I'm guessing that ids have to be unique, so I'm just adding 1000 to the id of the original card

@Suppress("MagicNumber")
val wildfireDeluxeEdition by lazy {
    CardDefinition(
        id = 1016,
        keyName = R.string.wildfire,
        value = 40,
        suit = Suit.FLAME,
        keyRule = R.string.wildfire_rules_deluxe_edition,
        cardSet = CardSet.DELUXE_EDITION
    )
}

@Suppress("MagicNumber")
val phoenixDeluxeEdition by lazy {
    CardDefinition(
        id = 1667,
        keyName = R.string.phoenix,
        value = 14,
        suit = Suit.BEAST,
        keyRule = R.string.phoenix_rules_deluxe_edition,
        cardSet = CardSet.DELUXE_EDITION,
        additionalSuits = listOf(Suit.FLAME, Suit.WEATHER)
    )
}
