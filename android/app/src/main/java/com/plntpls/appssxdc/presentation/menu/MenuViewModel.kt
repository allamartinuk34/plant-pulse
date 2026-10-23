package com.plntpls.appssxdc.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.PourUnit
import com.plntpls.appssxdc.domain.model.ShelfSettings
import com.plntpls.appssxdc.domain.model.WeekStart
import com.plntpls.appssxdc.domain.repository.ShelfRepository
import com.plntpls.appssxdc.domain.usecase.ApplyCareUseCase
import com.plntpls.appssxdc.domain.usecase.BuildScheduleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class MenuViewModel(
    private val shelf: ShelfRepository,
    private val buildSchedule: BuildScheduleUseCase,
    private val applyCare: ApplyCareUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val snapshot = withContext(Dispatchers.IO) { shelf.load() }
            _state.update { current ->
                current.copy(
                    isLoading = false,
                    plants = snapshot.plants,
                    log = snapshot.log.sortedByDescending { it.dayStamp },
                    schedule = buildSchedule(
                        snapshot.plants,
                        snapshot.today,
                        snapshot.settings.intervalNudge
                    ),
                    settings = snapshot.settings,
                    streakDays = snapshot.streakDays,
                    shelfHealth = healthOf(snapshot.plants),
                    dueCount = snapshot.plants.count { it.moisture <= GameConfig.DUE_MOISTURE },
                    nextDueDays = snapshot.plants.minOfOrNull { it.daysUntilDue } ?: 0,
                    today = snapshot.today,
                    weekNumber = (snapshot.today / 7) % 52 + 1,
                    weekdayIndex = buildSchedule.weekdayIndex(snapshot.today),
                    readFailed = snapshot.readFailed
                )
            }
        }
    }

    fun openPanel(panel: MenuPanel) {
        _state.update { it.copy(panel = panel, renaming = false) }
    }

    fun openPlant(plantId: String) {
        _state.update {
            it.copy(
                panel = MenuPanel.PROFILE,
                selectedPlantId = plantId,
                renaming = false,
                renameDraft = it.plants.firstOrNull { plant -> plant.id == plantId }?.name.orEmpty()
            )
        }
    }

    fun closePanel() {
        _state.update { it.copy(panel = null, renaming = false, showResetDialog = false) }
    }

    fun toggleRename() {
        _state.update { current ->
            current.copy(
                renaming = !current.renaming,
                renameDraft = current.selectedPlant?.name.orEmpty()
            )
        }
    }

    fun onRenameDraft(value: String) {
        _state.update { it.copy(renameDraft = value.take(16)) }
    }

    fun commitRename() {
        val current = _state.value
        val plant = current.selectedPlant ?: return
        val name = current.renameDraft.trim().uppercase()
        if (name.isEmpty()) {
            _state.update { it.copy(renaming = false) }
            return
        }
        val updated = current.plants.map { if (it.id == plant.id) it.copy(name = name) else it }
        persistPlants(updated)
        _state.update { it.copy(plants = updated, renaming = false) }
    }

    fun nudgeInterval(delta: Int) {
        val current = _state.value
        val plant = current.selectedPlant ?: return
        val next = (plant.intervalDays + delta)
            .coerceIn(GameConfig.MIN_INTERVAL_DAYS, GameConfig.MAX_INTERVAL_DAYS)
        if (next == plant.intervalDays) return
        val updated = current.plants.map { if (it.id == plant.id) it.copy(intervalDays = next) else it }
        persistPlants(updated)
        _state.update { it.copy(plants = updated, shelfHealth = healthOf(updated)) }
        rebuildSchedule(updated, current.settings)
    }

    fun waterSelected() {
        val current = _state.value
        val plant = current.selectedPlant ?: return
        val updated = applyCare(current.plants, plant.id)
        val entry = CareEntry(
            id = "care-" + current.today + "-" + plant.id,
            plantId = plant.id,
            plantName = plant.name,
            dayStamp = current.today,
            kind = CareKind.WATER,
            note = ""
        )
        val log = (listOf(entry) + current.log).take(GameConfig.MAX_LOG_ENTRIES)
        persistPlants(updated)
        persistLog(log)
        _state.update {
            it.copy(
                plants = updated,
                log = log,
                shelfHealth = healthOf(updated),
                dueCount = updated.count { plantItem -> plantItem.moisture <= GameConfig.DUE_MOISTURE },
                nextDueDays = updated.minOfOrNull { plantItem -> plantItem.daysUntilDue } ?: 0
            )
        }
        rebuildSchedule(updated, current.settings)
    }

    fun setPourUnit(unit: PourUnit) {
        updateSettings(_state.value.settings.copy(pourUnit = unit))
    }

    fun setWeekStart(weekStart: WeekStart) {
        updateSettings(_state.value.settings.copy(weekStart = weekStart))
    }

    fun setIntervalNudge(nudge: Int) {
        updateSettings(_state.value.settings.copy(intervalNudge = nudge.coerceIn(-1, 1)))
    }

    fun requestReset() {
        _state.update { it.copy(showResetDialog = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(showResetDialog = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { shelf.reset() }
            _state.update { it.copy(showResetDialog = false, panel = null, selectedPlantId = null) }
            refresh()
        }
    }

    private fun updateSettings(settings: ShelfSettings) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { shelf.saveSettings(settings) }
        }
        _state.update { it.copy(settings = settings) }
        rebuildSchedule(_state.value.plants, settings)
    }

    private fun rebuildSchedule(plants: List<Plant>, settings: ShelfSettings) {
        val today = _state.value.today
        _state.update {
            it.copy(schedule = buildSchedule(plants, today, settings.intervalNudge))
        }
    }

    private fun persistPlants(plants: List<Plant>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { shelf.savePlants(plants) }
        }
    }

    private fun persistLog(entries: List<CareEntry>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { shelf.saveLog(entries) }
        }
    }

    private fun healthOf(plants: List<Plant>): Int = if (plants.isEmpty()) {
        0
    } else {
        (plants.map { it.moisture.toDouble() }.average() * 100.0).roundToInt().coerceIn(0, 100)
    }
}
