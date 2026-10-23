package com.plntpls.appssxdc.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.di.AppViewModelFactory
import com.plntpls.appssxdc.core.theme.Accent
import com.plntpls.appssxdc.core.theme.Alert
import com.plntpls.appssxdc.core.theme.BgBase
import com.plntpls.appssxdc.core.theme.BgSunk
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.BottomActionDock
import com.plntpls.appssxdc.core.ui.DockAction
import com.plntpls.appssxdc.core.ui.EditorialRule
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.ScreenBackground
import com.plntpls.appssxdc.core.ui.StatCard
import com.plntpls.appssxdc.domain.model.RoundOutcome
import com.plntpls.appssxdc.domain.model.RoundSummary

@Composable
fun GameOverRoute(
    summary: RoundSummary,
    onCareAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(summary) {
        gameOverViewModel.refresh()
    }

    GameOverScreen(
        summary = summary,
        state = state,
        onCareAgain = onCareAgain,
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    summary: RoundSummary,
    state: GameOverUiState,
    onCareAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }
    val badgeScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 260f),
        label = "badgeEntry"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "contentEntry"
    )

    val verdict = when (summary.outcome) {
        RoundOutcome.COMPLETE -> stringResource(R.string.over_title_all)
        RoundOutcome.PARTIAL -> stringResource(R.string.over_title_some)
        RoundOutcome.EMPTY -> stringResource(R.string.over_title_none)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground(
            imageRes = R.drawable.bg_game,
            wash = Brush.verticalGradient(
                listOf(
                    Primary.copy(alpha = 0.18f),
                    BgBase.copy(alpha = 0.95f),
                    BgSunk.copy(alpha = 0.98f)
                )
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(color = Accent.copy(alpha = 0.18f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_leaf_badge),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(104.dp)
                        .graphicsLayer {
                            scaleX = badgeScale
                            scaleY = badgeScale
                            alpha = badgeScale
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = verdict,
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { liveRegion = LiveRegionMode.Polite }
                    .graphicsLayer { alpha = contentAlpha }
            )
            Spacer(modifier = Modifier.height(10.dp))
            EditorialRule()
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    R.string.over_subtitle,
                    summary.watered,
                    summary.attempted.coerceAtLeast(summary.watered)
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = contentAlpha }
            )
            Spacer(modifier = Modifier.height(22.dp))

            val showTopRow = summary.watered > 0 && summary.skipped > 0
            if (showTopRow) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        value = summary.watered.toString(),
                        label = stringResource(R.string.over_stat_watered),
                        accent = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        value = summary.skipped.toString(),
                        label = stringResource(R.string.over_stat_skipped),
                        accent = Secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = stringResource(R.string.percent_value, summary.shelfHealth),
                    label = stringResource(R.string.over_stat_health),
                    accent = Primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(R.string.days_short, summary.streakDays),
                    label = stringResource(R.string.over_stat_streak),
                    accent = Alert,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = state.stillDue.toString() + "/" + state.totalPlants.toString(),
                    label = stringResource(R.string.over_stat_due),
                    accent = Secondary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            PaperCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.over_notes_title),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (summary.notes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.over_notes_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                } else {
                    summary.notes.take(3).forEach { note ->
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.over_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            BottomActionDock(
                primaryText = stringResource(R.string.cta_care_again),
                onPrimary = onCareAgain,
                primaryIconRes = R.drawable.ic_refresh,
                secondary = listOf(
                    DockAction(
                        text = stringResource(R.string.cta_menu),
                        onClick = onMenu
                    )
                )
            )
        }
    }
}
