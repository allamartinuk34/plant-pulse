package com.plntpls.appssxdc.core.ui

import androidx.annotation.DrawableRes
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.domain.model.CareKind
import com.plntpls.appssxdc.domain.model.PlantSprite

@DrawableRes
fun plantSpriteRes(sprite: PlantSprite): Int = when (sprite) {
    PlantSprite.MONSTERA -> R.drawable.sprite_monstera
    PlantSprite.FERN -> R.drawable.sprite_fern
    PlantSprite.SUCCULENT -> R.drawable.sprite_succulent
}

@DrawableRes
fun careKindRes(kind: CareKind): Int = when (kind) {
    CareKind.WATER -> R.drawable.sprite_watering_can
    CareKind.MIST -> R.drawable.sprite_mist_spray
    CareKind.FEED -> R.drawable.sprite_soil_bag
}
