package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R


@Suppress("MagicNumber")
val dungeon by lazy {
    CardDefinition(
        1 + CardSet.BASE.numberOfCards,
        R.string.dungeon,
        7,
        Suit.BUILDING,
        R.string.dungeon_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val castle by lazy {
    CardDefinition(
        2 + CardSet.BASE.numberOfCards,
        R.string.castle,
        10,
        Suit.BUILDING,
        R.string.castle_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val crypt by lazy {
    CardDefinition(
        3 + CardSet.BASE.numberOfCards,
        R.string.crypt,
        21,
        Suit.BUILDING,
        R.string.crypt_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val chapel by lazy {
    CardDefinition(
        4 + CardSet.BASE.numberOfCards,
        R.string.chapel,
        2,
        Suit.BUILDING,
        R.string.chapel_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val garden by lazy {
    CardDefinition(
        5 + CardSet.BASE.numberOfCards,
        R.string.garden,
        11,
        Suit.LAND,
        R.string.garden_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val genie by lazy {
    CardDefinition(
        6 + CardSet.BASE.numberOfCards,
        R.string.genie,
        -50,
        Suit.OUTSIDER,
        R.string.genie_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val judge by lazy {
    CardDefinition(
        7 + CardSet.BASE.numberOfCards,
        R.string.judge,
        11,
        Suit.OUTSIDER,
        R.string.judge_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val angel by lazy {
    CardDefinition(
        8 + CardSet.BASE.numberOfCards,
        R.string.angel,
        16,
        Suit.OUTSIDER,
        R.string.angel_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val leprechaun by lazy {
    CardDefinition(
        9 + CardSet.BASE.numberOfCards,
        R.string.leprechaun,
        20,
        Suit.OUTSIDER,
        R.string.leprechaun_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val demon by lazy {
    CardDefinition(
        10 + CardSet.BASE.numberOfCards,
        R.string.demon,
        45,
        Suit.OUTSIDER,
        R.string.demon_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val darkQueen by lazy {
    CardDefinition(
        11 + CardSet.BASE.numberOfCards,
        R.string.dark_queen,
        10,
        Suit.UNDEAD,
        R.string.dark_queen_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val ghoul by lazy {
    CardDefinition(
        12 + CardSet.BASE.numberOfCards,
        R.string.ghoul,
        8,
        Suit.UNDEAD,
        R.string.ghoul_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val specter by lazy {
    CardDefinition(
        13 + CardSet.BASE.numberOfCards,
        R.string.specter,
        12,
        Suit.UNDEAD,
        R.string.specter_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val lich by lazy {
    CardDefinition(
        14 + CardSet.BASE.numberOfCards,
        R.string.lich,
        13,
        Suit.UNDEAD,
        R.string.lich_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val deathKnight by lazy {
    CardDefinition(
        15 + CardSet.BASE.numberOfCards,
        R.string.death_knight,
        14,
        Suit.UNDEAD,
        R.string.death_knight_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val bellTowerV2 by lazy {
    CardDefinition(
        16 + CardSet.BASE.numberOfCards,
        R.string.bell_tower, 8, Suit.BUILDING, R.string.bell_tower_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val fountainOfLifeV2 by lazy {
    CardDefinition(
        17 + CardSet.BASE.numberOfCards,
        R.string.fountain_of_life,
        1,
        Suit.FLOOD,
        R.string.fountain_of_life_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val greatFloodV2 by lazy {
    CardDefinition(
        18 + CardSet.BASE.numberOfCards,
        R.string.great_flood,
        32,
        Suit.FLOOD,
        R.string.great_flood_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val rangersV2 by lazy {
    CardDefinition(
        19 + CardSet.BASE.numberOfCards, R.string.rangers, 5,
        Suit.ARMY, R.string.rangers_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val necromancerV2 by lazy {
    CardDefinition(
        20 + CardSet.BASE.numberOfCards,
        R.string.necromancer,
        3,
        Suit.WIZARD,
        R.string.necromancer_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val worldTreeV2 by lazy {
    CardDefinition(
        21 + CardSet.BASE.numberOfCards,
        R.string.world_tree,
        2,
        Suit.ARTIFACT,
        R.string.world_tree_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val shapeshifterV2 by lazy {
    CardDefinition(
        22 + CardSet.BASE.numberOfCards,
        R.string.shapeshifter,
        0,
        Suit.WILD,
        R.string.shapeshifter_rules2,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val mirageV2 by lazy {
    CardDefinition(
        23 + CardSet.BASE.numberOfCards,
        R.string.mirage,
        0,
        Suit.WILD,
        R.string.mirage_rules2,
        CardSet.CURSED_HOARD
    )
}
