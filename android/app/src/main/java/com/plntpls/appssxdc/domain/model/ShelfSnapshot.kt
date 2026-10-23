package com.plntpls.appssxdc.domain.model

data class ShelfSnapshot(
    val plants: List<Plant>,
    val log: List<CareEntry>,
    val streakDays: Int,
    val lastCareDay: Int,
    val settings: ShelfSettings,
    val today: Int,
    val readFailed: Boolean
)
