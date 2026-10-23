package com.plntpls.appssxdc.domain.model

data class RoundSummary(
    val watered: Int,
    val skipped: Int,
    val shelfHealth: Int,
    val streakDays: Int,
    val notes: List<String>
) {
    val attempted: Int
        get() = watered + skipped

    val outcome: RoundOutcome
        get() = when {
            attempted == 0 -> RoundOutcome.EMPTY
            skipped == 0 -> RoundOutcome.COMPLETE
            else -> RoundOutcome.PARTIAL
        }
}

enum class RoundOutcome { COMPLETE, PARTIAL, EMPTY }
