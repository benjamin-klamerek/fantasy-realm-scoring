package com.klamerek.fantasyrealms.game

import android.content.Context
import com.klamerek.fantasyrealms.util.Preferences


object CardDefinitions {

    fun getAllById() : Map<Int, CardDefinition> {
        return getAll().map { definition -> definition.id to definition }.toMap()
    }

    fun getAll(): List<CardDefinition> {
        return get(buildingsOutsidersUndead = true, cursedItems = true);
    }

    fun get(context: Context): List<CardDefinition> {
        val buildingsOutsidersUndead = Preferences.getBuildingsOutsidersUndead(context)
        val cursedItems = Preferences.getCursedItems(context)
        return get(buildingsOutsidersUndead, cursedItems);
    }

    fun get(buildingsOutsidersUndead: Boolean, cursedItems: Boolean): List<CardDefinition> {
        val baseCards: List<CardDefinition> = listOf(
            if (buildingsOutsidersUndead) rangers else rangersV2,
            elvenArchers,
            dwarvishInfantry,
            lightCavalry,
            celestialKnights,
            protectionRune,
            if (buildingsOutsidersUndead) worldTree else worldTreeV2,
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
            if (buildingsOutsidersUndead) fountainOfLife else fountainOfLifeV2,
            waterElemental,
            island,
            swamp,
            if (buildingsOutsidersUndead) greatFlood else greatFloodV2,
            undergroundCaverns,
            earthElemental,
            forest,
            if (buildingsOutsidersUndead) bellTower else bellTowerV2,
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
            if (buildingsOutsidersUndead) shapeshifter else shapeshifterV2,
            if (buildingsOutsidersUndead) mirage else mirageV2,
            doppelganger,
            if (buildingsOutsidersUndead) necromancer else necromancerV2,
            elementalEnchantress,
            collector,
            beastmaster,
            warlockLord,
            jester
        )

        val newBaseCardsFromExpansion: List<CardDefinition> =
            if (buildingsOutsidersUndead) listOf(
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
            ) else emptyList()

        val cursedItemsDefinitions: List<CardDefinition> =
            if (cursedItems) emptyList() else emptyList()

        return newBaseCardsFromExpansion.plus(baseCards).plus(cursedItemsDefinitions)
    }

}

/**
 * Temporary solution until cyrillic characters are handled by MLKit text recognition
 */
//TODO THIS FIX
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
        "Jester" to jester
    )
}
