package com.plntpls.appssxdc.presentation.app

import androidx.lifecycle.ViewModel
import com.plntpls.appssxdc.core.navigation.Screen
import com.plntpls.appssxdc.domain.model.RoundSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val backStack = mutableListOf<Screen>()
    private var sessionCounter = 0

    fun showMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        _canGoBack.value = false
    }

    fun startCare() {
        sessionCounter += 1
        push(Screen.Menu)
        _screen.value = Screen.Game(sessionCounter)
    }

    fun careAgain() {
        sessionCounter += 1
        _screen.value = Screen.Game(sessionCounter)
    }

    fun finishRound(summary: RoundSummary) {
        _screen.value = Screen.GameOver(summary)
    }

    fun back() {
        if (backStack.isEmpty()) return
        val previous = backStack.removeAt(backStack.size - 1)
        _screen.value = previous
        _canGoBack.value = backStack.isNotEmpty()
    }

    private fun push(screen: Screen) {
        backStack.add(screen)
        _canGoBack.value = true
    }
}
