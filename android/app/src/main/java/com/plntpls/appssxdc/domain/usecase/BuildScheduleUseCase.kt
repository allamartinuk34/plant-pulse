package com.plntpls.appssxdc.domain.usecase

import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.Plant

data class ScheduleSlot(val plantId: String, val plantName: String, val kind: CareKind)

data class ScheduleDay(val dayStamp: Int, val weekdayIndex: Int, val offset: Int, val slots: List<ScheduleSlot>)

class BuildScheduleUseCase {

    operator fun invoke(plants: List<Plant>, today: Int, intervalNudge: Int): List<ScheduleDay> =
        (0 until GameConfig.SCHEDULE_DAYS).map { offset ->
            val stamp = today + offset
            val weekday = weekdayIndex(stamp)
            val slots = mutableListOf<ScheduleSlot>()
            plants.forEach { plant ->
                val span = (plant.intervalDays + intervalNudge).coerceAtLeast(GameConfig.MIN_INTERVAL_DAYS)
                val due = (span - plant.daysSinceWatered).coerceAtLeast(0)
                if (due == offset) {
                    slots.add(ScheduleSlot(plant.id, plant.name, CareKind.WATER))
                }
            }
            if (weekday == GameConfig.MIST_WEEKDAY) {
                plants.firstOrNull { it.sprite.name == "FERN" }?.let {
                    slots.add(ScheduleSlot(it.id, it.name, CareKind.MIST))
                }
            }
            if (stamp % GameConfig.FEED_PERIOD_DAYS == 0 && plants.isNotEmpty()) {
                val plant = plants[stamp % plants.size]
                slots.add(ScheduleSlot(plant.id, plant.name, CareKind.FEED))
            }
            ScheduleDay(stamp, weekday, offset, slots)
        }

    fun weekdayIndex(dayStamp: Int): Int {
        val raw = (dayStamp + 3) % 7
        return if (raw < 0) raw + 7 else raw
    }
}
