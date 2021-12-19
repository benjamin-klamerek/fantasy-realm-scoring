package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R

@Suppress("MagicNumber")
val jester by lazy {
    CardDefinition(
        666,
        R.string.jester,
        3,
        Suit.WIZARD,
        R.string.jester_rules,
        CardSet.PROMO
    )
}

@Suppress("MagicNumber")
val phoenix by lazy {
    CardDefinition(
        667,
        R.string.phoenix,
        14,
        Suit.BEAST,
        R.string.phoenix_rules,
        CardSet.PROMO,
        listOf(Suit.FLAME, Suit.WEATHER)
    )
}
