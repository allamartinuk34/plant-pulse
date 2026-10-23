package com.plntpls.appssxdc.presentation.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.plntpls.appssxdc.R
import com.plntpls.appssxdc.core.theme.Alert
import com.plntpls.appssxdc.core.theme.BorderHair
import com.plntpls.appssxdc.core.theme.Primary
import com.plntpls.appssxdc.core.theme.Secondary
import com.plntpls.appssxdc.core.theme.SurfaceTint
import com.plntpls.appssxdc.core.theme.TextMuted
import com.plntpls.appssxdc.core.theme.TextPrimary
import com.plntpls.appssxdc.core.theme.TextSecondary
import com.plntpls.appssxdc.core.ui.PanelScaffold
import com.plntpls.appssxdc.core.ui.PaperCard
import com.plntpls.appssxdc.core.ui.PlantAvatar
import com.plntpls.appssxdc.core.ui.StatCard
import com.plntpls.appssxdc.core.ui.pourText
import com.plntpls.appssxdc.domain.model.CareEntry
import com.plntpls.appssxdc.domain.model.Plant
import com.plntpls.appssxdc.domain.model.ShelfSettings

@Composable
fun PlantProfilePanel(
    visible: Boolean,
    plant: Plant?,
    entries: List<CareEntry>,
    today: Int,
    settings: ShelfSettings,
    renaming: Boolean,
    renameDraft: String,
    onToggleRename: () -> Unit,
    onRenameChange: (String) -> Unit,
    onRenameCommit: () -> Unit,
    onIntervalChange: (Int) -> Unit,
    onWater: () -> Unit,
    onDismiss: () -> Unit
) {
    PanelScaffold(
        visible = visible && plant != null,
        title = stringResource(R.string.profile_title),
        onDismiss = onDismiss,
        fillFraction = 0.72f
    ) {
        if (plant != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PlantAvatar(
                    sprite = plant.sprite,
                    description = stringResource(R.string.cd_plant, plant.name, plant.species),
                    plateSize = 72.dp,
                    spriteSize = 62.dp
                )
                Spacer(modifier = Modifier.size(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = plant.species,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = plant.spot,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = stringResource(R.string.percent_value, plant.moisturePercent),
                    label = stringResource(R.string.profile_moisture),
                    accent = Primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(R.string.days_short, plant.intervalDays),
                    label = stringResource(R.string.profile_every),
                    accent = Secondary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = pourText(plant.pourMl, settings.pourUnit),
                    label = stringResource(R.string.profile_pour),
                    accent = Alert,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (renaming) {
                OutlinedTextField(
                    value = renameDraft,
                    onValueChange = onRenameChange,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    label = {
                        Text(
                            text = stringResource(R.string.profile_rename_label),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SurfaceTint,
                        unfocusedContainerColor = SurfaceTint,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = BorderHair,
                        cursorColor = Primary,
                        focusedLabelColor = TextSecondary,
                        unfocusedLabelColor = TextSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onRenameCommit) {
                        Text(
                            text = stringResource(R.string.cta_save_name),
                            style = MaterialTheme.typography.labelLarge,
                            color = Primary
                        )
                    }
                }
            } else {
                TextButton(onClick = onToggleRename) {
                    Text(
                        text = stringResource(R.string.cta_rename),
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            PaperCard(
                modifier = Modifier.fillMaxWidth(),
                corner = 16.dp,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onIntervalChange(-1) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = stringResource(R.string.cd_interval_down),
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.profile_interval, plant.intervalDays),
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        onClick = { onIntervalChange(1) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.cd_interval_up),
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.profile_recent),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (entries.isEmpty()) {
                Text(
                    text = stringResource(R.string.profile_no_entries),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            } else {
                entries.forEach { entry ->
                    val ago = today - entry.dayStamp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Primary, RoundedCornerShape(3.dp))
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = if (entry.note.isEmpty()) plant.name else entry.note,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = stringResource(R.string.days_ago_short, ago.coerceAtLeast(0)),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = onWater) {
                Text(
                    text = stringResource(R.string.cta_water),
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
            }
        }
    }
}
