package com.plntpls.appssxdc.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.RoundSummary
import com.plntpls.appssxdc.domain.repository.ShelfRepository
import com.plntpls.appssxdc.domain.usecase.ApplyCareUseCase
import com.plntpls.appssxdc.domain.usecase.BuildCareQueueUseCase
import com.plntpls.appssxdc.domain.usecase.BuildRoundSummaryUseCase
import com.plntpls.appssxdc.domain.usecase.UpdateStreakUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val shelf: ShelfRepository,
    private val buildCareQueue: BuildCareQueueUseCase,
    private val applyCare: ApplyCareUseCase,
    private val buildRoundSummary: BuildRoundSummaryUseCase,
    private val updateStreak: UpdateStreakUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val finishedChannel = Channel<RoundSummary>(Channel.BUFFERED)
    val finished: Flow<RoundSummary> = finishedChannel.receiveAsFlow()

    private var activeSession = 0
    private var lastCareDay = 0
    private var roundClosed = false
    private var backstopJob: Job? = null

    fun beginSession(sessionId: Int) {
        if (activeSession == sessionId) return
        activeSession = sessionId
        roundClosed = false
        viewModelScope.launch {
            val snapshot = withContext(Dispatchers.IO) { shelf.load() }
            lastCareDay = snapshot.lastCareDay
            _state.value = GameUiState(
                ready = true,
                plants = snapshot.plants,
                queueIds = buildCareQueue(snapshot.plants),
                streakDays = snapshot.streakDays,
                log = snapshot.log.sortedByDescending { it.dayStamp },
                settings = snapshot.settings,
                today = snapshot.today
            )
            armBackstop()
        }
    }

    fun water() {
        val current = _state.value
        val plant = current.current ?: return
        if (current.isSaving) return
        val updated = applyCare(current.plants, plant.id)
        val entry = CareEntry(
            id = "care-" + current.today + "-" + plant.id + "-" + current.index,
            plantId = plant.id,
            plantName = plant.name,
            dayStamp = current.today,
            kind = CareKind.WATER,
            note = ""
        )
        val log = (listOf(entry) + current.log).take(GameConfig.MAX_LOG_ENTRIES)
        _state.update {
            it.copy(
                plants = updated,
                log = log,
                watered = it.watered + 1,
                isSaving = true
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shelf.savePlants(updated)
                shelf.saveLog(log)
            }
            delay(GameConfig.SAVE_FEEDBACK_MS)
            _state.update { it.copy(isSaving = false) }
            advance()
        }
        armBackstop()
    }

    fun skip() {
        if (_state.value.current == null) return
        _state.update { it.copy(skipped = it.skipped + 1) }
        advance()
        armBackstop()
    }

    fun openLog() {
        _state.update { it.copy(panel = GamePanel.LOG, composeMode = false) }
        armBackstop()
    }

    fun openNote() {
        _state.update { it.copy(panel = GamePanel.LOG, composeMode = true, noteDraft = "") }
        armBackstop()
    }

    fun closePanel() {
        _state.update { it.copy(panel = null, composeMode = false) }
        armBackstop()
    }

    fun onNoteDraft(value: String) {
        _state.update { it.copy(noteDraft = value.take(GameConfig.MAX_NOTE_CHARS)) }
    }

    fun saveNote() {
        val current = _state.value
        val plant = current.current ?: return
        val text = current.noteDraft.trim()
        if (text.isEmpty()) {
            _state.update { it.copy(panel = null, composeMode = false) }
            return
        }
        val entry = CareEntry(
            id = "note-" + current.today + "-" + plant.id + "-" + current.log.size,
            plantId = plant.id,
            plantName = plant.name,
            dayStamp = current.today,
            kind = CareKind.WATER,
            note = text
        )
        val log = (listOf(entry) + current.log).take(GameConfig.MAX_LOG_ENTRIES)
        viewModelScope.launch {
            withContext(Dispatchers.IO) { shelf.saveLog(log) }
        }
        _state.update {
            it.copy(
                log = log,
                roundNotes = it.roundNotes + text,
                noteDraft = "",
                panel = null,
                composeMode = false
            )
        }
        armBackstop()
    }

    fun pauseSession() {
        backstopJob?.cancel()
        backstopJob = null
    }

    override fun onCleared() {
        backstopJob?.cancel()
        backstopJob = null
        super.onCleared()
    }

    private fun advance() {
        val next = _state.value.index + 1
        _state.update { it.copy(index = next) }
        if (next >= _state.value.total) {
            closeRound()
        }
    }

    private fun armBackstop() {
        backstopJob?.cancel()
        backstopJob = viewModelScope.launch {
            delay(GameConfig.IDLE_BACKSTOP_MS)
            val remaining = _state.value.left
            if (remaining > 0) {
                _state.update { it.copy(skipped = it.skipped + remaining, index = it.total) }
            }
            closeRound()
        }
    }

    private fun closeRound() {
        if (roundClosed) return
        roundClosed = true
        backstopJob?.cancel()
        backstopJob = null
        val current = _state.value
        val today = current.today
        val streak = if (current.watered > 0) {
            updateStreak(current.streakDays, lastCareDay, today)
        } else {
            current.streakDays
        }
        val summary = buildRoundSummary(
            plants = current.plants,
            watered = current.watered,
            skipped = current.skipped,
            streakDays = streak,
            notes = current.roundNotes
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                shelf.savePlants(current.plants)
                shelf.saveLog(current.log)
                if (current.watered > 0) {
                    shelf.saveStreak(streak, today)
                }
            }
            finishedChannel.send(summary)
        }
        _state.update { it.copy(streakDays = streak) }
    }
}
