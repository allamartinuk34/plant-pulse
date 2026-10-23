package com.plntpls.appssxdc.data.repository

import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.data.local.PlantPrefs
import com.plntpls.appssxdc.data.sample.SampleData
import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.PlantSprite
import com.plntpls.appssxdc.domain.model.PourUnit
import com.plntpls.appssxdc.domain.model.ShelfSettings
import com.plntpls.appssxdc.domain.model.ShelfSnapshot
import com.plntpls.appssxdc.domain.model.WeekStart
import com.plntpls.appssxdc.domain.repository.ShelfRepository

class ShelfRepositoryImpl(private val prefs: PlantPrefs) : ShelfRepository {

    override fun today(): Int = (System.currentTimeMillis() / DAY_MILLIS).toInt()

    override fun load(): ShelfSnapshot {
        val today = today()
        if (!prefs.isSeeded()) {
            seed(today)
        }
        var failed = false
        var plants = runCatching { unpackPlants(prefs.readPlants(), today) }.getOrElse {
            failed = true
            emptyList()
        }
        if (plants.isEmpty()) {
            if (prefs.readPlants().isNotEmpty()) failed = true
            plants = SampleData.plants
        }
        val log = runCatching { unpackLog(prefs.readLog()) }.getOrElse {
            failed = true
            emptyList()
        }
        return ShelfSnapshot(
            plants = plants,
            log = log,
            streakDays = prefs.readStreak(),
            lastCareDay = prefs.readLastCareDay(),
            settings = readSettings(),
            today = today,
            readFailed = failed
        )
    }

    override fun savePlants(plants: List<Plant>) {
        prefs.writePlants(packPlants(plants, today()))
    }

    override fun saveLog(entries: List<CareEntry>) {
        prefs.writeLog(packLog(entries.take(GameConfig.MAX_LOG_ENTRIES)))
    }

    override fun saveStreak(streakDays: Int, lastCareDay: Int) {
        prefs.writeStreak(streakDays, lastCareDay)
    }

    override fun saveSettings(settings: ShelfSettings) {
        prefs.writeSettings(
            settings.pourUnit.name,
            settings.weekStart.name,
            settings.intervalNudge
        )
    }

    override fun reset() {
        prefs.clear()
        seed(today())
    }

    private fun seed(today: Int) {
        prefs.writePlants(packPlants(SampleData.plants, today))
        prefs.writeLog(packLog(SampleData.seededLog(today)))
        prefs.writeStreak(SampleData.SEEDED_STREAK, today - 1)
        prefs.writeSettings(PourUnit.ML.name, WeekStart.MON.name, 0)
        prefs.markSeeded()
    }

    private fun readSettings(): ShelfSettings {
        val unit = runCatching { PourUnit.valueOf(prefs.readUnits()) }.getOrDefault(PourUnit.ML)
        val week = runCatching { WeekStart.valueOf(prefs.readWeekStart()) }.getOrDefault(WeekStart.MON)
        return ShelfSettings(unit, week, prefs.readIntervalNudge().coerceIn(-1, 1))
    }

    private fun packPlants(plants: List<Plant>, today: Int): String =
        plants.joinToString(RECORD) { plant ->
            listOf(
                plant.id,
                clean(plant.name),
                clean(plant.species),
                clean(plant.spot),
                plant.intervalDays.toString(),
                plant.pourMl.toString(),
                plant.sprite.name,
                (today - plant.daysSinceWatered).toString()
            ).joinToString(FIELD)
        }

    private fun unpackPlants(packed: String, today: Int): List<Plant> {
        if (packed.isBlank()) return emptyList()
        return packed.split(RECORD).mapNotNull { record ->
            val parts = record.split(FIELD)
            if (parts.size < 8) return@mapNotNull null
            Plant(
                id = parts[0],
                name = parts[1],
                species = parts[2],
                spot = parts[3],
                intervalDays = parts[4].toInt().coerceIn(GameConfig.MIN_INTERVAL_DAYS, GameConfig.MAX_INTERVAL_DAYS),
                pourMl = parts[5].toInt(),
                sprite = PlantSprite.valueOf(parts[6]),
                daysSinceWatered = (today - parts[7].toInt()).coerceAtLeast(0)
            )
        }
    }

    private fun packLog(entries: List<CareEntry>): String =
        entries.joinToString(RECORD) { entry ->
            listOf(
                entry.id,
                entry.plantId,
                clean(entry.plantName),
                entry.dayStamp.toString(),
                entry.kind.name,
                clean(entry.note)
            ).joinToString(FIELD)
        }

    private fun unpackLog(packed: String): List<CareEntry> {
        if (packed.isBlank()) return emptyList()
        return packed.split(RECORD).mapNotNull { record ->
            val parts = record.split(FIELD)
            if (parts.size < 6) return@mapNotNull null
            CareEntry(
                id = parts[0],
                plantId = parts[1],
                plantName = parts[2],
                dayStamp = parts[3].toInt(),
                kind = CareKind.valueOf(parts[4]),
                note = parts[5]
            )
        }
    }

    private fun clean(raw: String): String =
        raw.replace(RECORD, " ").replace(FIELD, " ").trim()

    private companion object {
        const val DAY_MILLIS = 86_400_000L
        const val RECORD = ";"
        const val FIELD = "|"
    }
}
