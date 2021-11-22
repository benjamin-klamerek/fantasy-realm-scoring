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

@Suppress("MagicNumber")
val spyglass by lazy {
    CardDefinition(
        24 + CardSet.BASE.numberOfCards,
        R.string.spyglass,
        -1,
        Suit.CURSED_ITEM,
        R.string.spyglass_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val sarcophagus by lazy {
    CardDefinition(
        25 + CardSet.BASE.numberOfCards,
        R.string.sarcophagus,
        5,
        Suit.CURSED_ITEM,
        R.string.sarcophagus_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val blindfold by lazy {
    CardDefinition(
        26 + CardSet.BASE.numberOfCards,
        R.string.blindfold,
        5,
        Suit.CURSED_ITEM,
        R.string.blindfold_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val book_of_prophecy by lazy {
    CardDefinition(
        27 + CardSet.BASE.numberOfCards,
        R.string.book_of_prophecy,
        -1,
        Suit.CURSED_ITEM,
        R.string.book_of_prophecy_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val crystal_ball by lazy {
    CardDefinition(
        28 + CardSet.BASE.numberOfCards,
        R.string.crystal_ball,
        -1,
        Suit.CURSED_ITEM,
        R.string.crystal_ball_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val market_wagon by lazy {
    CardDefinition(
        29 + CardSet.BASE.numberOfCards,
        R.string.market_wagon,
        -2,
        Suit.CURSED_ITEM,
        R.string.market_wagon_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val backpack by lazy {
    CardDefinition(
        30 + CardSet.BASE.numberOfCards,
        R.string.backpack,
        -2,
        Suit.CURSED_ITEM,
        R.string.backpack_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val shovel by lazy {
    CardDefinition(
        31 + CardSet.BASE.numberOfCards,
        R.string.shovel,
        -2,
        Suit.CURSED_ITEM,
        R.string.shovel_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val sealed_vault by lazy {
    CardDefinition(
        32 + CardSet.BASE.numberOfCards,
        R.string.sealed_vault,
        -4,
        Suit.CURSED_ITEM,
        R.string.sealed_vault_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val crystal_lens by lazy {
    CardDefinition(
        33 + CardSet.BASE.numberOfCards,
        R.string.crystal_lens,
        -2,
        Suit.CURSED_ITEM,
        R.string.crystal_lens_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val larcenous_gloves by lazy {
    CardDefinition(
        34 + CardSet.BASE.numberOfCards,
        R.string.larcenous_gloves,
        -3,
        Suit.CURSED_ITEM,
        R.string.larcenous_gloves_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val junkyard_map by lazy {
    CardDefinition(
        35 + CardSet.BASE.numberOfCards,
        R.string.junkyard_map,
        -3,
        Suit.CURSED_ITEM,
        R.string.junkyard_map_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val winged_boots by lazy {
    CardDefinition(
        36 + CardSet.BASE.numberOfCards,
        R.string.winged_boots,
        -4,
        Suit.CURSED_ITEM,
        R.string.winged_boots_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val staff_of_transmutation by lazy {
    CardDefinition(
        37 + CardSet.BASE.numberOfCards,
        R.string.staff_of_transmutation,
        -4,
        Suit.CURSED_ITEM,
        R.string.staff_of_transmutation_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val rake by lazy {
    CardDefinition(
        38 + CardSet.BASE.numberOfCards,
        R.string.rake,
        -4,
        Suit.CURSED_ITEM,
        R.string.rake_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val treasure_chest by lazy {
    CardDefinition(
        39 + CardSet.BASE.numberOfCards,
        R.string.treasure_chest,
        -5,
        Suit.CURSED_ITEM,
        R.string.treasure_chest_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val fishhook by lazy {
    CardDefinition(
        40 + CardSet.BASE.numberOfCards,
        R.string.fishhook,
        -6,
        Suit.CURSED_ITEM,
        R.string.fishhook_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val repair_kit by lazy {
    CardDefinition(
        41 + CardSet.BASE.numberOfCards,
        R.string.repair_kit,
        -6,
        Suit.CURSED_ITEM,
        R.string.repair_kit_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val hourglass by lazy {
    CardDefinition(
        42 + CardSet.BASE.numberOfCards,
        R.string.hourglass,
        -7,
        Suit.CURSED_ITEM,
        R.string.hourglass_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val gold_mirror by lazy {
    CardDefinition(
        43 + CardSet.BASE.numberOfCards,
        R.string.gold_mirror,
        -8,
        Suit.CURSED_ITEM,
        R.string.gold_mirror_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val cauldron by lazy {
    CardDefinition(
        44 + CardSet.BASE.numberOfCards,
        R.string.cauldron,
        -9,
        Suit.CURSED_ITEM,
        R.string.cauldron_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val lantern by lazy {
    CardDefinition(
        45 + CardSet.BASE.numberOfCards,
        R.string.lantern,
        -10,
        Suit.CURSED_ITEM,
        R.string.lantern_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val portal by lazy {
    CardDefinition(
        46 + CardSet.BASE.numberOfCards,
        R.string.portal,
        -20,
        Suit.CURSED_ITEM,
        R.string.portal_rules,
        CardSet.CURSED_HOARD
    )
}

@Suppress("MagicNumber")
val wishing_ring by lazy {
    CardDefinition(
        47 + CardSet.BASE.numberOfCards,
        R.string.wishing_ring,
        -30,
        Suit.CURSED_ITEM,
        R.string.wishing_ring_rules,
        CardSet.CURSED_HOARD
    )
}
