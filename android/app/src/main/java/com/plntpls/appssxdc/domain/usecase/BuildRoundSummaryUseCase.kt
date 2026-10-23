package com.plntpls.appssxdc.domain.usecase

import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.RoundSummary
import kotlin.math.roundToInt

class BuildRoundSummaryUseCase {

    operator fun invoke(
        plants: List<Plant>,
        watered: Int,
        skipped: Int,
        streakDays: Int,
        notes: List<String>
    ): RoundSummary {
        val health = if (plants.isEmpty()) {
            0
        } else {
            (plants.map { it.moisture.toDouble() }.average() * 100.0).roundToInt()
        }
        return RoundSummary(
            watered = watered,
            skipped = skipped,
            shelfHealth = health.coerceIn(0, 100),
            streakDays = streakDays,
            notes = notes
        )
    }
}
