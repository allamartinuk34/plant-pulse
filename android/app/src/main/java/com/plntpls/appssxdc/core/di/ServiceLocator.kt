package com.plntpls.appssxdc.core.di

import android.content.Context
import com.plntpls.appssxdc.data.local.PlantPrefs
import com.plntpls.appssxdc.data.repository.ShelfRepositoryImpl
import com.plntpls.appssxdc.domain.repository.ShelfRepository
import com.plntpls.appssxdc.domain.usecase.ApplyCareUseCase
import com.plntpls.appssxdc.domain.usecase.BuildCareQueueUseCase
import com.plntpls.appssxdc.domain.usecase.BuildRoundSummaryUseCase
import com.plntpls.appssxdc.domain.usecase.BuildScheduleUseCase
import com.plntpls.appssxdc.domain.usecase.UpdateStreakUseCase

object ServiceLocator {

    private var shelf: ShelfRepository? = null

    val buildCareQueue = BuildCareQueueUseCase()
    val applyCare = ApplyCareUseCase()
    val buildRoundSummary = BuildRoundSummaryUseCase()
    val buildSchedule = BuildScheduleUseCase()
    val updateStreak = UpdateStreakUseCase()

    fun init(context: Context) {
        if (shelf == null) {
            shelf = ShelfRepositoryImpl(PlantPrefs(context.applicationContext))
        }
    }

    fun shelf(): ShelfRepository = checkNotNull(shelf)
}
