package com.plntpls.appssxdc.data.sample

import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.PlantSprite

object SampleData {

    const val SEEDED_STREAK = 6

    val plants: List<Plant> = listOf(
        Plant("atlas", "ATLAS", "MONSTERA DELICIOSA", "WINDOW SHELF", 7, 250, PlantSprite.MONSTERA, 7),
        Plant("mist", "MIST", "BOSTON FERN", "BATHROOM SILL", 3, 120, PlantSprite.FERN, 3),
        Plant("cascade", "CASCADE", "GOLDEN POTHOS", "KITCHEN HOOK", 5, 180, PlantSprite.MONSTERA, 5),
        Plant("vesper", "VESPER", "PEACE LILY", "BEDROOM CORNER", 6, 200, PlantSprite.MONSTERA, 4),
        Plant("pebble", "PEBBLE", "JADE SUCCULENT", "DESK", 10, 80, PlantSprite.SUCCULENT, 7),
        Plant("sentinel", "SENTINEL", "SNAKE PLANT", "HALLWAY", 14, 150, PlantSprite.SUCCULENT, 2)
    )

    fun seededLog(today: Int): List<CareEntry> = listOf(
        CareEntry("seed-1", "atlas", "ATLAS", today - 2, CareKind.WATER, "NEW LEAF UNFURLING"),
        CareEntry("seed-2", "mist", "MIST", today - 3, CareKind.MIST, "LEAF TIPS DRY - MISTED"),
        CareEntry("seed-3", "pebble", "PEBBLE", today - 6, CareKind.FEED, "REPOTTED INTO TERRACOTTA")
    )
}
