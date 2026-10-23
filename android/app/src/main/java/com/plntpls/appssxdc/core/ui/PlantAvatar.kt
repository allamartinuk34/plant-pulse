package com.plntpls.appssxdc.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.domain.model.PlantSprite

@Composable
fun PlantAvatar(
    sprite: PlantSprite,
    description: String?,
    modifier: Modifier = Modifier,
    plateSize: Dp = 64.dp,
    spriteSize: Dp = 56.dp
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .size(plateSize)
            .background(color = SurfaceTint, shape = shape)
            .border(width = 1.dp, color = BorderHair, shape = shape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(plantSpriteRes(sprite)),
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(spriteSize)
        )
    }
}
