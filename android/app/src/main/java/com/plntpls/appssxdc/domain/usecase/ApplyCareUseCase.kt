package com.plntpls.appssxdc.domain.usecase

import com.plntpls.appssxdc.domain.model.Plant

class ApplyCareUseCase {

    operator fun invoke(plants: List<Plant>, plantId: String): List<Plant> =
        plants.map { if (it.id == plantId) it.copy(daysSinceWatered = 0) else it }
}
