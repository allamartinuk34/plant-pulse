package com.plntpls.appssxdc.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.di.AppViewModelFactory
import com.plntpls.appssxdc.core.theme.Accent
import com.plntpls.appssxdc.core.theme.Alert
import com.plntpls.appssxdc.core.theme.BgBase
import com.plntpls.appssxdc.core.theme.BgSunk
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.SurfacePaper
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.BottomActionDock
import com.plntpls.appssxdc.core.ui.DockAction
import com.plntpls.appssxdc.core.ui.MoistureRing
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.ScreenBackground
import com.plntpls.appssxdc.core.ui.StatCard
import com.plntpls.appssxdc.core.ui.pourText
import com.plntpls.appssxdc.core.ui.plantSpriteRes
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.RoundSummary
import com.plntpls.appssxdc.presentation.panels.CareLogPanel

@Composable
fun GameRoute(
    sessionId: Int,
    onBack: () -> Unit,
    onRoundFinished: (RoundSummary) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(sessionId) {
        gameViewModel.beginSession(sessionId)
    }

    LaunchedEffect(Unit) {
        gameViewModel.finished.collect { summary ->
            onRoundFinished(summary)
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pauseSession() }
    }

    GameScreen(
        state = state,
        onBack = onBack,
        onWater = gameViewModel::water,
        onSkip = gameViewModel::skip,
        onOpenLog = gameViewModel::openLog,
        onOpenNote = gameViewModel::openNote,
        onClosePanel = gameViewModel::closePanel,
        onNoteDraft = gameViewModel::onNoteDraft,
        onSaveNote = gameViewModel::saveNote
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onBack: () -> Unit,
    onWater: () -> Unit,
    onSkip: () -> Unit,
    onOpenLog: () -> Unit,
    onOpenNote: () -> Unit,
    onClosePanel: () -> Unit,
    onNoteDraft: (String) -> Unit,
    onSaveNote: () -> Unit
) {
    BackHandler(enabled = state.panel != null) {
        onClosePanel()
    }

    val progressDescription = stringResource(R.string.sd_round_progress, state.index, state.total)
    val progress by animateFloatAsState(
        targetValue = state.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "roundProgress"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground(
            imageRes = R.drawable.bg_game,
            wash = Brush.verticalGradient(
                listOf(BgBase.copy(alpha = 0.90f), BgSunk.copy(alpha = 0.96f))
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.cd_back),
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.game_caption),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = stringResource(R.string.game_position, state.position, state.total),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
                IconButton(onClick = onOpenLog, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_list),
                        contentDescription = stringResource(R.string.cd_care_log),
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderHair)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(color = BorderHair, shape = RoundedCornerShape(3.dp))
                    .semantics { stateDescription = progressDescription }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(listOf(Primary, Accent)),
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = state.current,
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { it / 4 }) + fadeIn() togetherWith
                            slideOutHorizontally(targetOffsetX = { -it / 4 }) + fadeOut()
                    },
                    label = "plantCard"
                ) { plant ->
                    if (plant == null) {
                        Text(
                            text = stringResource(R.string.game_round_done),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        PlantCard(plant = plant, state = state)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = SurfacePaper.copy(alpha = 0.70f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(width = 1.dp, color = BorderHair, shape = RoundedCornerShape(14.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StripReading(
                    label = stringResource(R.string.game_stat_watered),
                    value = state.watered.toString(),
                    modifier = Modifier.weight(1f)
                )
                StripReading(
                    label = stringResource(R.string.game_stat_left),
                    value = state.left.toString(),
                    modifier = Modifier.weight(1f)
                )
                StripReading(
                    label = stringResource(R.string.game_stat_streak),
                    value = stringResource(R.string.streak_days, state.streakDays),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            BottomActionDock(
                primaryText = stringResource(R.string.cta_water_now),
                onPrimary = onWater,
                primaryEnabled = state.current != null && !state.isSaving,
                primaryIconRes = R.drawable.ic_check,
                secondary = listOf(
                    DockAction(
                        text = stringResource(R.string.cta_skip),
                        onClick = onSkip,
                        enabled = state.current != null
                    ),
                    DockAction(
                        text = stringResource(R.string.cta_add_note),
                        onClick = onOpenNote,
                        enabled = state.current != null
                    )
                )
            )
        }

        CareLogPanel(
            visible = state.panel == GamePanel.LOG,
            entries = state.log,
            today = state.today,
            composeMode = state.composeMode,
            draft = state.noteDraft,
            plantName = state.current?.name,
            onDraftChange = onNoteDraft,
            onSaveNote = onSaveNote,
            onDismiss = onClosePanel
        )
    }
}

@Composable
private fun PlantCard(plant: Plant, state: GameUiState) {
    val moistureDescription = stringResource(R.string.sd_moisture, plant.moisturePercent)
    val pop by animateFloatAsState(
        targetValue = if (state.isSaving) 1.06f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 320f),
        label = "spritePop"
    )
    PaperCard(
        modifier = Modifier.fillMaxWidth(),
        corner = 24.dp,
        contentPadding = PaddingValues(20.dp)
    ) {
        MoistureRing(
            progress = plant.moisture,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(plantSpriteRes(plant.sprite)),
                    contentDescription = stringResource(
                        R.string.cd_plant,
                        plant.name,
                        plant.species
                    ),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(84.dp)
                        .graphicsLayer {
                            scaleX = pop
                            scaleY = pop
                        }
                )
                Text(
                    text = stringResource(R.string.percent_value, plant.moisturePercent),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    modifier = Modifier.semantics { stateDescription = moistureDescription }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = plant.name,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.game_subtitle, plant.species, plant.spot),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                value = stringResource(R.string.days_ago_short, plant.daysSinceWatered),
                label = stringResource(R.string.game_fact_last),
                accent = Alert,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = stringResource(R.string.days_short, plant.intervalDays),
                label = stringResource(R.string.game_fact_every),
                accent = Primary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                value = pourText(plant.pourMl, state.settings.pourUnit),
                label = stringResource(R.string.game_fact_pour),
                accent = Secondary,
                modifier = Modifier.weight(1f)
            )
        }
        if (state.currentNotes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.currentNotes.forEach { note ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(color = SurfaceTint, shape = RoundedCornerShape(16.dp))
                            .border(
                                width = 1.dp,
                                color = BorderHair,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = note,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StripReading(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
    }
}
