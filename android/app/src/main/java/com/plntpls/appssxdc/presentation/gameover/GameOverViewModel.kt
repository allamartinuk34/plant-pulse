package com.plntpls.appssxdc.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.domain.repository.ShelfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class GameOverUiState(
    val totalPlants: Int = 0,
    val stillDue: Int = 0,
    val loggedEntries: Int = 0
)

class GameOverViewModel(private val shelf: ShelfRepository) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val snapshot = withContext(Dispatchers.IO) { shelf.load() }
            _state.value = GameOverUiState(
                totalPlants = snapshot.plants.size,
                stillDue = snapshot.plants.count { it.moisture <= GameConfig.DUE_MOISTURE },
                loggedEntries = snapshot.log.size
            )
        }
    }
}
