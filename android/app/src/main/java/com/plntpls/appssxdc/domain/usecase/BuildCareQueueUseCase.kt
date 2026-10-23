package com.plntpls.appssxdc.domain.usecase

import com.plntpls.appssxdc.core.config.GameConfig
import com.plntpls.appssxdc.domain.model.Plant

class BuildCareQueueUseCase {

    operator fun invoke(plants: List<Plant>): List<String> {
        if (plants.isEmpty()) return emptyList()
        val due = plants.filter { it.moisture <= GameConfig.DUE_MOISTURE }.sortedBy { it.moisture }
        val queue = if (due.isEmpty()) plants.sortedBy { it.moisture }.take(2) else due
        return queue.take(GameConfig.MAX_ROUND_PLANTS).map { it.id }
    }
}
