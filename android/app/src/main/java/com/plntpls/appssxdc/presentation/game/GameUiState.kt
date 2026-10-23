package com.plntpls.appssxdc.presentation.game

import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.ShelfSettings

enum class GamePanel { LOG }

data class GameUiState(
    val ready: Boolean = false,
    val plants: List<Plant> = emptyList(),
    val queueIds: List<String> = emptyList(),
    val index: Int = 0,
    val watered: Int = 0,
    val skipped: Int = 0,
    val streakDays: Int = 0,
    val isSaving: Boolean = false,
    val panel: GamePanel? = null,
    val composeMode: Boolean = false,
    val noteDraft: String = "",
    val roundNotes: List<String> = emptyList(),
    val log: List<CareEntry> = emptyList(),
    val settings: ShelfSettings = ShelfSettings(),
    val today: Int = 0
) {
    val total: Int
        get() = queueIds.size

    val current: Plant?
        get() = queueIds.getOrNull(index)?.let { id -> plants.firstOrNull { it.id == id } }

    val position: Int
        get() = (index + 1).coerceAtMost(total.coerceAtLeast(1))

    val progress: Float
        get() = if (total == 0) 0f else index.toFloat() / total.toFloat()

    val left: Int
        get() = (total - index).coerceAtLeast(0)

    val currentNotes: List<String>
        get() {
            val id = current?.id ?: return emptyList()
            return log.filter { it.plantId == id && it.note.isNotEmpty() }.map { it.note }.take(2)
        }

    val todayLog: List<CareEntry>
        get() = log.filter { it.dayStamp == today }
}
