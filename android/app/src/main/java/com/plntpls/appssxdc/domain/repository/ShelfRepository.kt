package com.plntpls.appssxdc.domain.repository

import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.ShelfSettings
import com.plntpls.appssxdc.domain.model.ShelfSnapshot

interface ShelfRepository {
    fun load(): ShelfSnapshot
    fun savePlants(plants: List<Plant>)
    fun saveLog(entries: List<CareEntry>)
    fun saveStreak(streakDays: Int, lastCareDay: Int)
    fun saveSettings(settings: ShelfSettings)
    fun reset()
    fun today(): Int
}
