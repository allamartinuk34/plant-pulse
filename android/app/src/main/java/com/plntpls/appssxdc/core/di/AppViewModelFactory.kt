package com.plntpls.appssxdc.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.plntpls.appssxdc.presentation.app.AppViewModel
import com.plntpls.appssxdc.presentation.game.GameViewModel
import com.plntpls.appssxdc.presentation.gameover.GameOverViewModel
import com.plntpls.appssxdc.presentation.menu.MenuViewModel
import com.plntpls.appssxdc.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                shelf = ServiceLocator.shelf(),
                buildSchedule = ServiceLocator.buildSchedule,
                applyCare = ServiceLocator.applyCare
            )

            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                shelf = ServiceLocator.shelf(),
                buildCareQueue = ServiceLocator.buildCareQueue,
                applyCare = ServiceLocator.applyCare,
                buildRoundSummary = ServiceLocator.buildRoundSummary,
                updateStreak = ServiceLocator.updateStreak
            )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                shelf = ServiceLocator.shelf()
            )

            else -> AppViewModel()
        }
        return created as T
    }
}
