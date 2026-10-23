package com.plntpls.appssxdc.domain.model

import com.plntpls.appssxdc.core.config.GameConfig

enum class PlantSprite { MONSTERA, FERN, SUCCULENT }

enum class PlantStatus { THIRSTY, DUE, HEALTHY }

data class Plant(
    val id: String,
    val name: String,
    val species: String,
    val spot: String,
    val intervalDays: Int,
    val pourMl: Int,
    val sprite: PlantSprite,
    val daysSinceWatered: Int
) {
    val moisture: Float
        get() {
            val span = intervalDays.coerceAtLeast(1).toFloat()
            return (1f - daysSinceWatered.toFloat() / span).coerceIn(0f, 1f)
        }

    val moisturePercent: Int
        get() = (moisture * 100f).toInt()

    val daysUntilDue: Int
        get() = (intervalDays - daysSinceWatered).coerceAtLeast(0)

    val status: PlantStatus
        get() = when {
            moisture <= GameConfig.THIRSTY_MOISTURE -> PlantStatus.THIRSTY
            moisture <= GameConfig.DUE_MOISTURE -> PlantStatus.DUE
            else -> PlantStatus.HEALTHY
        }
}
