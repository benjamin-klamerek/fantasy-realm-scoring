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
