package com.klamerek.fantasyrealms.game

import com.klamerek.fantasyrealms.R
import com.klamerek.fantasyrealms.Strings

val empty by lazy { CardDefinition(-1, "", -1, Suit.WILD, "") }

val rangers by lazy {
    CardDefinition(
        1, Strings.get(R.string.rangers), 5,
        Suit.ARMY, Strings.get(R.string.rangers_rules)
    )
}
val elvenArchers by lazy { CardDefinition(2, Strings.get(R.string.elven_archers), 10, Suit.ARMY, Strings.get(R.string.elven_archers_rules)) }
val dwarvishInfantry by lazy {
    CardDefinition(
        3,
        Strings.get(R.string.dwarvish_infantry),
        15,
        Suit.ARMY,
        Strings.get(R.string.dwarvish_infantry_rules)
    )
}
val lightCavalry by lazy { CardDefinition(4, Strings.get(R.string.light_cavalry), 17, Suit.ARMY, Strings.get(R.string.light_cavalry_rules)) }
val celestialKnights by lazy {
    CardDefinition(
        5,
        Strings.get(R.string.celestial_knights),
        20,
        Suit.ARMY,
        Strings.get(R.string.celestial_knights_rules)
    )
}
val protectionRune by lazy {
    CardDefinition(
        6,
        Strings.get(R.string.protection_rune),
        1,
        Suit.ARTIFACT,
        Strings.get(R.string.protection_rune_rules)
    )
}
val worldTree by lazy {
    CardDefinition(
        7,
        Strings.get(R.string.world_tree),
        2,
        Suit.ARTIFACT,
        Strings.get(R.string.world_tree_rules)
    )
}
val bookOfChanges by lazy {
    CardDefinition(
        8,
        Strings.get(R.string.book_of_changes),
        3,
        Suit.ARTIFACT,
        Strings.get(R.string.book_of_changes_rules)
    )
}
val shieldOfKeth by lazy {
    CardDefinition(
        9,
        Strings.get(R.string.shield_of_keth),
        4,
        Suit.ARTIFACT,
        Strings.get(R.string.shield_of_keth_rules)
    )
}
val gemOfOrder by lazy {
    CardDefinition(
        10,
        Strings.get(R.string.gem_of_order),
        5,
        Suit.ARTIFACT,
        Strings.get(R.string.gem_of_order_rules)
    )
}
val warhorse by lazy {
    CardDefinition(11, Strings.get(R.string.warhorse), 6, Suit.BEAST, Strings.get(R.string.warhorse_rules))
}
val unicorn by lazy {
    CardDefinition(
        12,
        Strings.get(R.string.unicorn),
        9,
        Suit.BEAST,
        Strings.get(R.string.unicorn_rules)
    )
}
val hydra by lazy { CardDefinition(13, Strings.get(R.string.hydra), 12, Suit.BEAST, Strings.get(R.string.hydra_rules)) }
val dragon by lazy { CardDefinition(14, Strings.get(R.string.dragon), 30, Suit.BEAST, Strings.get(R.string.dragon_rules)) }
val basilisk by lazy {
    CardDefinition(
        15,
        Strings.get(R.string.basilisk),
        35,
        Suit.BEAST,
        Strings.get(R.string.basilisk_rules)
    )
}
val candle by lazy {
    CardDefinition(
        16,
        Strings.get(R.string.candle),
        2,
        Suit.FLAME,
        Strings.get(R.string.candle_rules)
    )
}
val fireElemental by lazy { CardDefinition(17, Strings.get(R.string.fire_elemental), 4, Suit.FLAME, Strings.get(R.string.fire_elemental_rules)) }
val forge by lazy { CardDefinition(18, Strings.get(R.string.forge), 9, Suit.FLAME, Strings.get(R.string.forge_rules)) }
val lightning by lazy { CardDefinition(19, Strings.get(R.string.lightning), 11, Suit.FLAME, Strings.get(R.string.lightning_rules)) }
val wildfire by lazy {
    CardDefinition(
        20,
        Strings.get(R.string.wildfire),
        40,
        Suit.FLAME,
        Strings.get(R.string.wildfire_rules)
    )
}
val fountainOfLife by lazy {
    CardDefinition(
        21,
        Strings.get(R.string.fountain_of_life),
        1,
        Suit.FLOOD,
        Strings.get(R.string.fountain_of_life_rules)
    )
}
val waterElemental by lazy { CardDefinition(22, Strings.get(R.string.water_elemental), 4, Suit.FLOOD, Strings.get(R.string.water_elemental_rules)) }
val island by lazy { CardDefinition(23, Strings.get(R.string.island), 14, Suit.FLOOD, Strings.get(R.string.island_rules)) }
val swamp by lazy { CardDefinition(24, Strings.get(R.string.swamp), 18, Suit.FLOOD, Strings.get(R.string.swamp_rules)) }
val greatFlood by lazy {
    CardDefinition(
        25,
        Strings.get(R.string.great_flood),
        32,
        Suit.FLOOD,
        Strings.get(R.string.great_flood_rules)
    )
}
val earthElemental by lazy { CardDefinition(26, Strings.get(R.string.earth_elemental), 4, Suit.LAND, Strings.get(R.string.earth_elemental_rules)) }
val undergroundCaverns by lazy {
    CardDefinition(
        27,
        Strings.get(R.string.underground_caverns),
        6,
        Suit.LAND,
        Strings.get(R.string.underground_caverns_rules)
    )
}
val forest by lazy { CardDefinition(28, Strings.get(R.string.forest), 7, Suit.LAND, Strings.get(R.string.forest_rules)) }
val bellTower by lazy { CardDefinition(29, Strings.get(R.string.bell_tower), 8, Suit.LAND, Strings.get(R.string.bell_tower_rules)) }
val mountain by lazy {
    CardDefinition(
        30,
        Strings.get(R.string.mountain),
        9,
        Suit.LAND,
        Strings.get(R.string.mountain_rules)
    )
}
val princess by lazy {
    CardDefinition(
        31,
        Strings.get(R.string.princess),
        2,
        Suit.LEADER,
        Strings.get(R.string.princess_rules)
    )
}
val warlord by lazy {
    CardDefinition(
        32,
        Strings.get(R.string.warlord),
        4,
        Suit.LEADER,
        Strings.get(R.string.warlord_rules)
    )
}
val queen by lazy {
    CardDefinition(
        33,
        Strings.get(R.string.queen),
        6,
        Suit.LEADER,
        Strings.get(R.string.queen_rules)
    )
}
val king by lazy {
    CardDefinition(
        34,
        Strings.get(R.string.king),
        8,
        Suit.LEADER,
        Strings.get(R.string.king_rules)
    )
}
val empress by lazy {
    CardDefinition(
        35,
        Strings.get(R.string.empress),
        15,
        Suit.LEADER,
        Strings.get(R.string.empress_rules)
    )
}
val magicWand by lazy {
    CardDefinition(
        36,
        Strings.get(R.string.magic_wand), 1, Suit.WEAPON, Strings.get(R.string.magic_wand_rules)
    )
}
val elvenLongbow by lazy {
    CardDefinition(
        37,
        Strings.get(R.string.elven_longbow),
        3,
        Suit.WEAPON,
        Strings.get(R.string.elven_longbow_rules)
    )
}
val swordOfKeth by lazy {
    CardDefinition(
        38,
        Strings.get(R.string.sword_of_keth),
        7,
        Suit.WEAPON,
        Strings.get(R.string.sword_of_keth_rules)
    )
}
val warship by lazy {
    CardDefinition(
        39,
        Strings.get(R.string.warship),
        23,
        Suit.WEAPON,
        Strings.get(R.string.warship_rules)
    )
}
val warDirigible by lazy {
    CardDefinition(
        40,
        Strings.get(R.string.war_dirigible),
        35,
        Suit.WEAPON,
        Strings.get(R.string.war_dirigible_rules)
    )
}
val airElemental by lazy {
    CardDefinition(
        41,
        Strings.get(R.string.air_elemental), 4, Suit.WEATHER, Strings.get(R.string.air_elemental_rules)
    )
}
val rainstorm by lazy {
    CardDefinition(
        42,
        Strings.get(R.string.rainstorm),
        8,
        Suit.WEATHER,
        Strings.get(R.string.rainstorm_rules)
    )
}
val whirlwind by lazy {
    CardDefinition(
        43,
        Strings.get(R.string.whirlwind),
        13,
        Suit.WEATHER,
        Strings.get(R.string.whirlwind_rules)
    )
}
val smoke by lazy {
    CardDefinition(
        44,
        Strings.get(R.string.smoke), 27, Suit.WEATHER, Strings.get(R.string.smoke_rules)
    )
}
val blizzard by lazy {
    CardDefinition(
        45,
        Strings.get(R.string.blizzard),
        30,
        Suit.WEATHER,
        Strings.get(R.string.blizzard_rules)
    )
}
val shapeshifter by lazy {
    CardDefinition(
        46,
        Strings.get(R.string.shapeshifter),
        0,
        Suit.WILD,
        Strings.get(R.string.shapeshifter_rules)
    )
}
val mirage by lazy {
    CardDefinition(
        47,
        Strings.get(R.string.mirage),
        0,
        Suit.WILD,
        Strings.get(R.string.mirage_rules)
    )
}
val doppelganger by lazy {
    CardDefinition(
        48,
        Strings.get(R.string.doppelganger),
        0,
        Suit.WILD,
        Strings.get(R.string.doppelganger_rules)
    )
}
val necromancer by lazy {
    CardDefinition(
        49,
        Strings.get(R.string.necromancer),
        3,
        Suit.WIZARD,
        Strings.get(R.string.necromancer_rules)
    )
}
val elementalEnchantress by lazy {
    CardDefinition(
        50,
        Strings.get(R.string.elemental_enchantress),
        5,
        Suit.WIZARD,
        Strings.get(R.string.elemental_enchantress_rules)
    )
}
val collector by lazy {
    CardDefinition(
        51,
        Strings.get(R.string.collector),
        7,
        Suit.WIZARD,
        Strings.get(R.string.collector_rules)
    )
}
val beastmaster by lazy {
    CardDefinition(
        52,
        Strings.get(R.string.beastmaster),
        9,
        Suit.WIZARD,
        Strings.get(R.string.beastmaster_rules)
    )
}
val warlockLord by lazy {
    CardDefinition(
        53,
        Strings.get(R.string.warlock_lord),
        25,
        Suit.WIZARD,
        Strings.get(R.string.warlock_lord_rules)
    )
}

val allDefinitions by lazy {
    listOf(
        rangers,
        elvenArchers,
        dwarvishInfantry,
        lightCavalry,
        celestialKnights,
        protectionRune,
        worldTree,
        bookOfChanges,
        shieldOfKeth,
        gemOfOrder,
        warhorse,
        unicorn,
        undergroundCaverns,
        hydra,
        dragon,
        basilisk,
        candle,
        fireElemental,
        forge,
        lightning,
        wildfire,
        fountainOfLife,
        waterElemental,
        island,
        swamp,
        greatFlood,
        earthElemental,
        forest,
        bellTower,
        mountain,
        princess,
        warlord,
        queen,
        king,
        empress,
        magicWand,
        elvenLongbow,
        swordOfKeth,
        warship,
        warDirigible,
        airElemental,
        rainstorm,
        whirlwind,
        smoke,
        blizzard,
        shapeshifter,
        mirage,
        doppelganger,
        necromancer,
        elementalEnchantress,
        collector,
        beastmaster,
        warlockLord
    )
}

val cardsById = allDefinitions.map { definition -> definition.id to definition }.toMap()