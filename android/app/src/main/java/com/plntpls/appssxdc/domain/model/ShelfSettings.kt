package com.plntpls.appssxdc.domain.model

enum class PourUnit { ML, CUPS }

enum class WeekStart { MON, SUN }

data class ShelfSettings(
    val pourUnit: PourUnit = PourUnit.ML,
    val weekStart: WeekStart = WeekStart.MON,
    val intervalNudge: Int = 0
)
