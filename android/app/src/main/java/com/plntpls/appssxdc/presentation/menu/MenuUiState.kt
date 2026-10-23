package com.plntpls.appssxdc.presentation.menu

import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.ShelfSettings
import com.plntpls.appssxdc.domain.usecase.ScheduleDay

enum class MenuPanel { PROFILE, LOG, SCHEDULE, SETTINGS }

data class MenuUiState(
    val isLoading: Boolean = true,
    val plants: List<Plant> = emptyList(),
    val log: List<CareEntry> = emptyList(),
    val schedule: List<ScheduleDay> = emptyList(),
    val settings: ShelfSettings = ShelfSettings(),
    val streakDays: Int = 0,
    val shelfHealth: Int = 0,
    val dueCount: Int = 0,
    val nextDueDays: Int = 0,
    val today: Int = 0,
    val weekNumber: Int = 1,
    val weekdayIndex: Int = 0,
    val panel: MenuPanel? = null,
    val selectedPlantId: String? = null,
    val renaming: Boolean = false,
    val renameDraft: String = "",
    val showResetDialog: Boolean = false,
    val readFailed: Boolean = false
) {
    val selectedPlant: Plant?
        get() = plants.firstOrNull { it.id == selectedPlantId }

    val selectedEntries: List<CareEntry>
        get() = log.filter { it.plantId == selectedPlantId }.take(3)
}
