package com.plntpls.appssxdc.domain.usecase

class UpdateStreakUseCase {

    operator fun invoke(streakDays: Int, lastCareDay: Int, today: Int): Int = when {
        lastCareDay == today -> streakDays.coerceAtLeast(1)
        today - lastCareDay == 1 -> streakDays + 1
        else -> 1
    }
}
