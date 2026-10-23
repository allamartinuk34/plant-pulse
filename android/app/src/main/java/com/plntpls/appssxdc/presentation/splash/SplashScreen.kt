package com.plntpls.appssxdc.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.di.AppViewModelFactory
import com.plntpls.appssxdc.core.theme.Accent
import com.plntpls.appssxdc.core.theme.OnPrimary
import com.plntpls.appssxdc.core.theme.SplashFoot
import com.plntpls.appssxdc.core.theme.SplashMid
import com.plntpls.appssxdc.core.theme.SplashTop
import com.plntpls.appssxdc.core.theme.SplashVignette
import kotlin.math.sin

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }

    val idle = rememberInfiniteTransition(label = "splashIdle")
    val breath by idle.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )
    val ringAlpha by idle.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.42f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringAlpha"
    )
    val drift by idle.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )
    val emblemScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.82f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 220f),
        label = "emblemEntry"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 520, delayMillis = 160),
        label = "titleEntry"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            SplashTop.copy(alpha = 0.94f),
                            SplashMid.copy(alpha = 0.90f),
                            SplashFoot.copy(alpha = 0.88f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, SplashVignette.copy(alpha = 0.55f)),
                        radius = 900f
                    )
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val motes = 3
            for (index in 0 until motes) {
                val phase = (drift + index * 0.33f) % 1f
                val y = size.height * (0.92f - phase * 0.7f)
                val lateral = sin((phase * 6.28f) + index.toFloat()) * size.width * 0.12f
                val x = size.width * 0.5f + lateral + (index - 1) * size.width * 0.18f
                drawCircle(
                    color = Accent.copy(alpha = 0.12f),
                    radius = (6f + index * 2f) * density,
                    center = Offset(x, y)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(164.dp)
                    .background(color = Accent.copy(alpha = 0.10f), shape = CircleShape)
                    .border(width = 1.dp, color = Accent.copy(alpha = ringAlpha), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_leaf_badge),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(132.dp)
                        .graphicsLayer {
                            scaleX = breath * emblemScale
                            scaleY = breath * emblemScale
                            alpha = emblemScale
                        }
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.splash_title_line1),
                style = MaterialTheme.typography.displaySmall,
                color = OnPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Text(
                text = stringResource(R.string.splash_title_line2),
                style = MaterialTheme.typography.displaySmall,
                color = Accent,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = Accent.copy(alpha = 0.80f),
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha }
            )
            Spacer(modifier = Modifier.height(40.dp))
            LinearProgressIndicator(
                color = Accent,
                trackColor = OnPrimary.copy(alpha = 0.20f),
                modifier = Modifier
                    .width(148.dp)
                    .height(3.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_status),
                style = MaterialTheme.typography.labelSmall,
                color = OnPrimary.copy(alpha = 0.55f),
                textAlign = TextAlign.Center
            )
        }
    }
}
