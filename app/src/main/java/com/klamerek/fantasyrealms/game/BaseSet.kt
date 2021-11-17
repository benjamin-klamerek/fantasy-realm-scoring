package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R

@Suppress("MagicNumber")
val empty by lazy {
    CardDefinition(
        -1, -1, -1, Suit.WILD, -1,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val rangers by lazy {
    CardDefinition(
        25, R.string.rangers, 5,
        Suit.ARMY, R.string.rangers_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val elvenArchers by lazy {
    CardDefinition(
        22, R.string.elven_archers, 10, Suit.ARMY, R.string.elven_archers_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val dwarvishInfantry by lazy {
    CardDefinition(
        24,
        R.string.dwarvish_infantry,
        15,
        Suit.ARMY,
        R.string.dwarvish_infantry_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val lightCavalry by lazy {
    CardDefinition(
        23,
        R.string.light_cavalry,
        17,
        Suit.ARMY,
        R.string.light_cavalry_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val celestialKnights by lazy {
    CardDefinition(
        21,
        R.string.celestial_knights,
        20,
        Suit.ARMY,
        R.string.celestial_knights_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val protectionRune by lazy {
    CardDefinition(
        50,
        R.string.protection_rune,
        1,
        Suit.ARTIFACT,
        R.string.protection_rune_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val worldTree by lazy {
    CardDefinition(
        48,
        R.string.world_tree,
        2,
        Suit.ARTIFACT,
        R.string.world_tree_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val bookOfChanges by lazy {
    CardDefinition(
        49,
        R.string.book_of_changes,
        3,
        Suit.ARTIFACT,
        R.string.book_of_changes_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val shieldOfKeth by lazy {
    CardDefinition(
        46,
        R.string.shield_of_keth,
        4,
        Suit.ARTIFACT,
        R.string.shield_of_keth_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val gemOfOrder by lazy {
    CardDefinition(
        47,
        R.string.gem_of_order,
        5,
        Suit.ARTIFACT,
        R.string.gem_of_order_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val warhorse by lazy {
    CardDefinition(38, R.string.warhorse, 6, Suit.BEAST, R.string.warhorse_rules, CardSet.BASE)
}

@Suppress("MagicNumber")
val unicorn by lazy {
    CardDefinition(
        36,
        R.string.unicorn,
        9,
        Suit.BEAST,
        R.string.unicorn_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val hydra by lazy {
    CardDefinition(
        40, R.string.hydra, 12, Suit.BEAST, R.string.hydra_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val dragon by lazy {
    CardDefinition(
        39, R.string.dragon, 30, Suit.BEAST, R.string.dragon_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val basilisk by lazy {
    CardDefinition(
        37,
        R.string.basilisk,
        35,
        Suit.BEAST,
        R.string.basilisk_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val candle by lazy {
    CardDefinition(
        17,
        R.string.candle,
        2,
        Suit.FLAME,
        R.string.candle_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val fireElemental by lazy {
    CardDefinition(
        20, R.string.fire_elemental, 4, Suit.FLAME, R.string.fire_elemental_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val forge by lazy {
    CardDefinition(
        18, R.string.forge, 9, Suit.FLAME, R.string.forge_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val lightning by lazy {
    CardDefinition(
        19, R.string.lightning, 11, Suit.FLAME, R.string.lightning_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val wildfire by lazy {
    CardDefinition(
        16,
        R.string.wildfire,
        40,
        Suit.FLAME,
        R.string.wildfire_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val fountainOfLife by lazy {
    CardDefinition(
        6,
        R.string.fountain_of_life,
        1,
        Suit.FLOOD,
        R.string.fountain_of_life_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val waterElemental by lazy {
    CardDefinition(
        10, R.string.water_elemental, 4, Suit.FLOOD, R.string.water_elemental_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val island by lazy {
    CardDefinition(
        9, R.string.island, 14, Suit.FLOOD, R.string.island_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val swamp by lazy {
    CardDefinition(
        7, R.string.swamp, 18, Suit.FLOOD, R.string.swamp_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val greatFlood by lazy {
    CardDefinition(
        8,
        R.string.great_flood,
        32,
        Suit.FLOOD,
        R.string.great_flood_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val earthElemental by lazy {
    CardDefinition(
        5, R.string.earth_elemental, 4, Suit.LAND, R.string.earth_elemental_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val undergroundCaverns by lazy {
    CardDefinition(
        2,
        R.string.underground_caverns,
        6,
        Suit.LAND,
        R.string.underground_caverns_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val forest by lazy {
    CardDefinition(
        4, R.string.forest, 7, Suit.LAND, R.string.forest_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val bellTower by lazy {
    CardDefinition(
        3, R.string.bell_tower, 8, Suit.LAND, R.string.bell_tower_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val mountain by lazy {
    CardDefinition(
        1,
        R.string.mountain,
        9,
        Suit.LAND,
        R.string.mountain_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val princess by lazy {
    CardDefinition(
        33,
        R.string.princess,
        2,
        Suit.LEADER,
        R.string.princess_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val warlord by lazy {
    CardDefinition(
        34,
        R.string.warlord,
        4,
        Suit.LEADER,
        R.string.warlord_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val queen by lazy {
    CardDefinition(
        32,
        R.string.queen,
        6,
        Suit.LEADER,
        R.string.queen_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val king by lazy {
    CardDefinition(
        31,
        R.string.king,
        8,
        Suit.LEADER,
        R.string.king_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val empress by lazy {
    CardDefinition(
        35,
        R.string.empress,
        15,
        Suit.LEADER,
        R.string.empress_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val magicWand by lazy {
    CardDefinition(
        42,
        R.string.magic_wand, 1, Suit.WEAPON, R.string.magic_wand_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val elvenLongbow by lazy {
    CardDefinition(
        44,
        R.string.elven_longbow,
        3,
        Suit.WEAPON,
        R.string.elven_longbow_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val swordOfKeth by lazy {
    CardDefinition(
        43,
        R.string.sword_of_keth,
        7,
        Suit.WEAPON,
        R.string.sword_of_keth_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val warship by lazy {
    CardDefinition(
        41,
        R.string.warship,
        23,
        Suit.WEAPON,
        R.string.warship_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val warDirigible by lazy {
    CardDefinition(
        45,
        R.string.war_dirigible,
        35,
        Suit.WEAPON,
        R.string.war_dirigible_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val airElemental by lazy {
    CardDefinition(
        15,
        R.string.air_elemental, 4, Suit.WEATHER, R.string.air_elemental_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val rainstorm by lazy {
    CardDefinition(
        11,
        R.string.rainstorm,
        8,
        Suit.WEATHER,
        R.string.rainstorm_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val whirlwind by lazy {
    CardDefinition(
        14,
        R.string.whirlwind,
        13,
        Suit.WEATHER,
        R.string.whirlwind_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val smoke by lazy {
    CardDefinition(
        13,
        R.string.smoke, 27, Suit.WEATHER, R.string.smoke_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val blizzard by lazy {
    CardDefinition(
        12,
        R.string.blizzard,
        30,
        Suit.WEATHER,
        R.string.blizzard_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val shapeshifter by lazy {
    CardDefinition(
        51,
        R.string.shapeshifter,
        0,
        Suit.WILD,
        R.string.shapeshifter_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val mirage by lazy {
    CardDefinition(
        52,
        R.string.mirage,
        0,
        Suit.WILD,
        R.string.mirage_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val doppelganger by lazy {
    CardDefinition(
        53,
        R.string.doppelganger,
        0,
        Suit.WILD,
        R.string.doppelganger_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val necromancer by lazy {
    CardDefinition(
        28,
        R.string.necromancer,
        3,
        Suit.WIZARD,
        R.string.necromancer_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val elementalEnchantress by lazy {
    CardDefinition(
        30,
        R.string.elemental_enchantress,
        5,
        Suit.WIZARD,
        R.string.elemental_enchantress_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val collector by lazy {
    CardDefinition(
        26,
        R.string.collector,
        7,
        Suit.WIZARD,
        R.string.collector_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val beastmaster by lazy {
    CardDefinition(
        27,
        R.string.beastmaster,
        9,
        Suit.WIZARD,
        R.string.beastmaster_rules,
        CardSet.BASE
    )
}

@Suppress("MagicNumber")
val warlockLord by lazy {
    CardDefinition(
        29,
        R.string.warlock_lord,
        25,
        Suit.WIZARD,
        R.string.warlock_lord_rules,
        CardSet.BASE
    )
}

