package com.klamerek.fantasyrealms.game

import android.content.Context
import com.klamerek.fantasyrealms.util.Preferences


object CardDefinitions {

    /**
     * Warning, this method gives ALL card definitions (meaning that you can see many times the same card if it has many versions)
     */
    fun getAllById(): Map<Int, CardDefinition> {
        return getAll().map { definition -> definition.id to definition }.toMap()
    }

    /**
     * Warning, this method gives ALL card definitions (meaning that you can see many times the same card if it has many versions)
     */
    fun getAll(): List<CardDefinition> {
        return getBaseCards().plus(getCardsV2()).plus(getCursedHoardNewCards()).plus(getCursedItems()).plus(getPromo())
    }

    fun get(context: Context): List<CardDefinition> {
        val buildingsOutsidersUndead = Preferences.getBuildingsOutsidersUndead(context)
        val cursedItems = Preferences.getCursedItems(context)
        return get(buildingsOutsidersUndead, cursedItems)
    }

    private fun getCardsV2(): List<CardDefinition> {
        return listOf(
            rangersV2,
            worldTreeV2,
            fountainOfLifeV2,
            greatFloodV2,
            bellTowerV2,
            shapeshifterV2,
            mirageV2,
            necromancerV2
        )
    }

    private fun getBaseCards(): List<CardDefinition> {
        return listOf(
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
            undergroundCaverns,
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

    private fun getPromo(): List<CardDefinition> {
        return listOf(jester, phoenix)
    }

    private fun getCursedHoardNewCards(): List<CardDefinition> {
        return listOf(
            dungeon,
            castle,
            crypt,
            chapel,
            garden,
            genie,
            judge,
            angel,
            leprechaun,
            demon,
            darkQueen,
            ghoul,
            specter,
            lich,
            deathKnight
        )
    }

    fun getCursedItems(): List<CardDefinition> {
        return listOf(spyglass,
                sarcophagus,
                blindfold,
                book_of_prophecy,
                crystal_ball,
                market_wagon,
                backpack,
                shovel,
                sealed_vault,
                crystal_lens,
                larcenous_gloves,
                junkyard_map,
                winged_boots,
                staff_of_transmutation,
                rake,
                treasure_chest,
                fishhook,
                repair_kit,
                hourglass,
                gold_mirror,
                cauldron,
                lantern,
                portal,
                wishing_ring)
    }

    fun get(buildingsOutsidersUndead: Boolean, cursedItems: Boolean): List<CardDefinition> {
        val cardV2Names = getCardsV2().map { it.name() }.toList()
        val baseCards = if (buildingsOutsidersUndead)
            getBaseCards().minus(getBaseCards().filter { cardV2Names.contains(it.name()) }.toSet())
                .plus(getCardsV2()) else getBaseCards()

        val newBaseCardsFromExpansion: List<CardDefinition> =
            if (buildingsOutsidersUndead) getCursedHoardNewCards() else emptyList()

        val cursedItemsDefinitions: List<CardDefinition> =
            if (cursedItems) getCursedItems() else emptyList()

        return newBaseCardsFromExpansion.plus(baseCards).plus(cursedItemsDefinitions).plus(getPromo())
    }

}

/**
 * Temporary solution until cyrillic characters are handled by MLKit text recognition
 */
val allDefinitionsRussian by lazy {
    mapOf(
        "IMApa" to hydra,
        "BeMKUM NOTO" to greatFlood,
        "Ayx orHs" to fireElemental,
        "AMBCHb" to rainstorm,
        "YapoaenKa" to elementalEnchantress,
        "3BeponOB" to beastmaster,
        "Ky3HMLa" to forge,
        "Aupukaő1b" to warDirigible,
        "CooupaTeAb" to collector,
        "AAMHHIM AYK" to elvenLongbow,
        "HekpoMaHT" to necromancer,
        "KoponcBa" to queen,
        "boAOTO" to swamp,
        "MoAHS" to lightning,
        "EaMHOpor" to unicorn,
        "PbIuap" to celestialKnights,
        "OcrpoB" to island,
        "Ayx BOAbl" to waterElemental,
        "ABoMHUK" to doppelganger,
        "CBeua" to candle,
        "Ayx 3eMAM" to earthElemental,
        "Mey KeTa" to swordOfKeth,
        "Erepa" to rangers,
        "MupakK" to mirage,
        "KHura nepemeH" to bookOfChanges,
        "THOMbI IexOTa" to dwarvishInfantry,
        "06opoTeHb" to shapeshifter,
        "Ileuepa" to undergroundCaverns,
        "AbIM" to smoke,
        "IpMHUCcca" to princess,
        "9bdb-CTpeAKW" to elvenArchers,
        "MeTeAb" to blizzard,
        "YparaH" to whirlwind,
        "BocBOM KOHb" to warhorse,
        "lec" to forest,
        "Ke3" to magicWand,
        "KameHb nopAAka" to gemOfOrder,
        "Apakkap" to warship,
        "BoeHaya1bHMK" to warlord,
        "Topa" to mountain,
        "ApakoH" to dragon,
        "DOHTAH KU3HM" to fountainOfLife,
        "Ayx BO34yxa" to airElemental,
        "MMneparpuua" to empress,
        "MeuOM KeTa" to shieldOfKeth,
        "KonokoAbHA" to bellTower,
        "PyHHBIM oốcper" to protectionRune,
        "Iloxap" to wildfire,
        "KopoAb" to king,
        "UepHOKHITKHUK" to warlockLord,
        "KaBanepua" to lightCavalry,
        "MupOBOC ACPeBo" to worldTree,
        "BacuaMCK" to basilisk,
        "Jester" to jester,
        "Phoenix" to phoenix
    )
}
