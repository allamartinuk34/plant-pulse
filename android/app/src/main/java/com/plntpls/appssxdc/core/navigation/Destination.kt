package com.plntpls.appssxdc.core.navigation

import com.plntpls.appssxdc.domain.model.RoundSummary

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val sessionId: Int) : Screen
    data class GameOver(val summary: RoundSummary) : Screen
}
